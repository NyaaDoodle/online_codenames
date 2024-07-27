package game.join;

import client.exceptions.JoinGameException;
import client.game.instance.GameRole;
import game.room.GameRoom;
import client.game.room.PlayerState;
import client.game.structure.Team;
import input.InputController;
import client.lobby.LobbyController;
import client.lobby.game.list.GameList;
import client.lobby.game.list.GameListingData;
import okhttp3.Request;
import okhttp3.RequestBody;
import ui.UIElements;

import utils.OtherUtils;
import utils.constants.Constants;
import utils.http.HttpClientUtils;
import utils.json.JSONUtils;

public class GameJoiner {
    private static final int NUMBER_OF_ROLES = 2;
    private static final int DEFINER_OPTION = 1;

    private static final String GAME_FULL_ERROR = "GAME_FULL";
    private static final String TEAM_FULL_ERROR = "TEAM_FULL";
    private static final String ROLE_FULL_ERROR = "ROLE_FULL";
    private static final String BAD_JSON_ERROR = "BAD_SYNTAX";
    private static final String ALREADY_JOINED_GAME_ERROR = "ALREADY_JOINED";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON_TYPE = "application/json";

    public static void selectGame() {
        GameList gameList = getGameList();
        if (gameList == null) { return; }
        if (gameList.getGameAmount() < 1) {
            System.out.println("No games are available that fit the criteria currently.");
            return;
        }

        boolean joinedGame = false;
        boolean exitedMenu = false;
        int input;
        Team selectedTeam;
        GameRole selectedRole;
        JoinerMenuState menuState = JoinerMenuState.GAME;
        TempPlayerState tempPlayerState = new TempPlayerState();
        PlayerState playerState = null;
        // TODO refactor this mess...
        while (!joinedGame && !exitedMenu) {
            switch (menuState) {
                case GAME:
                    selectGameMessage();
                    printGameList(gameList);
                    input = getInput(gameList.getGameAmount());
                    if (input == Constants.GO_BACK_NUM) { menuState = JoinerMenuState.EXIT; }
                    else {
                        tempPlayerState.setSelectedGame(gameList.getGameList().get(input - 1));
                        menuState = JoinerMenuState.TEAM;
                    }
                    break;

                case TEAM:
                    selectTeamMessage();
                    printTeamList(tempPlayerState.getSelectedGame(), true);
                    input = getInput(tempPlayerState.getSelectedGame().getTeams().size());
                    if (input == Constants.GO_BACK_NUM) { menuState = JoinerMenuState.GAME; }
                    else {
                        selectedTeam = tempPlayerState.getSelectedGame().getTeams().get(input - 1);
                        if (checkTeamFull(tempPlayerState.getSelectedGame(), selectedTeam)) {
                            selectedTeamIsFullMessage();
                        }
                        else {
                            tempPlayerState.setSelectedTeam(selectedTeam);
                            menuState = JoinerMenuState.ROLE;
                        }
                    }
                    break;

                case ROLE:
                    selectRoleMessage();
                    printRoles(tempPlayerState.getSelectedGame(), tempPlayerState.getSelectedTeam(), true);
                    input = getInput(NUMBER_OF_ROLES);
                    if (input == Constants.GO_BACK_NUM) { menuState = JoinerMenuState.TEAM; }
                    else {
                        selectedRole = (input == DEFINER_OPTION) ? GameRole.DEFINER : GameRole.GUESSER;
                        if (checkRoleFull(tempPlayerState.getSelectedGame(), tempPlayerState.getSelectedTeam(), selectedRole)) {
                            selectedRoleIsFullMessage();
                        }
                        else {
                            tempPlayerState.setSelectedRole(input == 1 ? GameRole.DEFINER : GameRole.GUESSER);
                            try {
                                playerState = new PlayerState(tempPlayerState.getGameName(), tempPlayerState.getTeamName(),
                                        tempPlayerState.getSelectedRole());
                                sendSelectionRequest(playerState);
                                joinedGame = true;
                            } catch (JoinGameException e) {
                                menuState = handleException(e);
                                gameList = getGameList();
                                if (gameList == null) { return; }
                                if (gameList.getGameAmount() < 1) {
                                    System.out.println("No games are available that fit the criteria currently.");
                                    return;
                                }
                            } catch (Exception e) {
                                UIElements.unexpectedExceptionMessage(e);
                                return;
                            }
                        }
                    }
                    break;

                case EXIT:
                    exitedMenu = true;
                    break;
            }
        }
        if (joinedGame) {
            GameRoom gameRoom = new GameRoom(gameList.getGameListing(tempPlayerState.getGameName()), playerState);
            gameRoom.goToGameRoom();
        }
    }

    private static void selectGameMessage() {
        System.out.println("First, select a game to join, according to the number at the start of each game listing:");
        UIElements.goBackOptionMessage();
    }

    private static void selectTeamMessage() {
        System.out.println("Select a team to join, according to the number at the start of each team listing:");
        UIElements.goBackOptionMessage();
    }

    private static void selectRoleMessage() {
        System.out.println("Select a role to join: 1 for definer, 2 for guesser");
        UIElements.goBackOptionMessage();
    }

    private static void selectedTeamIsFullMessage() {
        System.out.println("The selected team is full. Select a different team:");
    }

    private static void selectedRoleIsFullMessage() {
        System.out.println("The selected role is full. Select a different role:");
    }

    private static GameList getGameList() {
        try {
            return LobbyController.getGameListOnlyPending();
        } catch (Exception e) {
            UIElements.unexpectedExceptionMessage(e);
            return null;
        }
    }

    private static int getInput(final int upperLimit) {
        return InputController.intMenuInput(OtherUtils.makeSetFromOneToN(upperLimit));
    }

    private static boolean checkTeamFull(final GameListingData gameListing, final Team team) {
        return gameListing.isTeamFull(team);
    }

    private static boolean checkRoleFull(final GameListingData gameListingData, final Team team, final GameRole role) {
        return (role == GameRole.DEFINER) ? gameListingData.areDefinersFull(team) : gameListingData.areGuessersFull(team);
    }

    private static void printGameList(final GameList gameList) {
        for (int i = 0; i < gameList.getGameAmount(); i++) {
            final GameListingData game = gameList.getGameList().get(i);
            final int listingNum = i + 1;
            System.out.println("(" + listingNum + ")");
            System.out.println("Game name: " + game.getName());
            System.out.println("Teams:");
            printTeamList(game, false);
            System.out.println();
        }
    }

    private static void printTeamList(final GameListingData gameListing, final boolean withIndices) {
        for (int i = 0; i < gameListing.getTeams().size(); i++) {
            final Team team = gameListing.getTeams().get(i);
            final int listingNum = i + 1;
            if (withIndices) {
                System.out.println("(" + listingNum + ")");
            }
            System.out.println("Team name: " + team.getName());
            System.out.println("Word count: " + team.getCardCount());
            printRoles(gameListing, team, false);
        }
    }
    private static void printRoles(final GameListingData gameListing, final Team team, final boolean withIndices) {
        final int connectedDefiners = gameListing.getConnectedDefinersByTeam(team.getName());
        final int connectedGuessers = gameListing.getConnectedGuessersByTeam(team.getName());
        final String indexDefiners = withIndices ? "(1) " : "";
        final String indexGuessers = withIndices ? "(2) " : "";
        System.out.println(indexDefiners + "Connected definers: " + connectedDefiners + " / " + team.getDefinersCount());
        System.out.println(indexGuessers + "Connected guessers: " + connectedGuessers + " / " + team.getGuessersCount());
    }

    private static void sendSelectionRequest(final PlayerState playerState) throws Exception {
        final String json = JSONUtils.toJson(playerState);
        final String finalUrl = Constants.BASE_URL + Constants.JOIN_GAME_RESOURCE_URI;
        final RequestBody body = RequestBody.create(json.getBytes());
        Request req = new Request.Builder().post(body).url(finalUrl).addHeader(CONTENT_TYPE, JSON_TYPE).build();
        HttpClientUtils.sendJoinGameRequest(req);
    }

    private static JoinerMenuState handleException(final JoinGameException e) {
        JoinerMenuState state;
        switch (e.getErrorType()) {
            case GAME_FULL_ERROR:
                state = JoinerMenuState.GAME;
                break;
            case TEAM_FULL_ERROR:
                state = JoinerMenuState.TEAM;
                break;
            case ROLE_FULL_ERROR:
                state = JoinerMenuState.ROLE;
                break;
            default:
                state = JoinerMenuState.EXIT;
                break;
        }
        System.out.println(e.getMessage());
        return state;
    }
}
