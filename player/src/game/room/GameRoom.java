package game.room;

import client.exceptions.NoPlayerStatusAtServerException;
import client.game.data.GameData;
import client.game.instance.GameRole;
import client.game.instance.Hint;
import client.game.instance.MoveEvent;
import client.game.instance.TeamLeftEntry;
import client.game.instance.data.GameInstanceData;
import client.game.room.ClientGameRoom;
import client.game.room.PlayerState;
import client.game.structure.GameStructure;
import client.game.structure.Team;
import client.utils.constants.ClientConstants;
import client.utils.http.ClientHttpClientUtils;
import input.InputController;
import client.lobby.game.list.GameListingData;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.OtherUtils;
import utils.json.JSONUtils;

import java.util.Objects;

public class GameRoom extends ClientGameRoom {
    private static final String HINT_WORDS_PARAMETER_NAME = "hint-words";
    private static final String GUESSES_COUNT_PARAMETER_NAME = "number";
    private static final String CARD_NUMBER_PARAMETER_NAME = "card-number";
    private static final String GAME_URL = ClientConstants.BASE_URL + ClientConstants.GAME_RESOURCE_URI;

    public GameRoom(@NotNull GameListingData gameListingData, @NotNull PlayerState playerState) {
        super(gameListingData, playerState);
    }

    public void makeMove() {
        GameData gameData = getGameData();
        final PlayerState playerState = getPlayerState();
        boolean quitGuessing = false;
        if (gameData.getGameListingData().isGameActive()) {
            final GameInstanceData gameInstanceData = gameData.getGameInstanceData();
            assert gameInstanceData != null;
            final Team currentTeam = gameInstanceData.getCurrentTurn();
            final GameRole currentRole = gameInstanceData.getCurrentRole();
            if (currentTeam.getName().equals(playerState.getTeam())) {
                HttpUrl finalUrl = null;
                if (currentRole.equals(playerState.getRole())) {
                    final int upperLimit = gameInstanceData.getWordCards().size();
                    switch (getPlayerState().getRole()) {
                        case DEFINER:
                            UIElements.printBoard(gameInstanceData, GameRole.DEFINER);
                            System.out.println("Please enter the hint phrase:");
                            final String hintWords = InputController.getStringTrimmed();
                            System.out.println("Please enter the number of guesses:");
                            final int hintNumber = InputController.intMenuInputRegular(OtherUtils.makeSetFromOneToN(upperLimit));
                            finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_URL)).newBuilder()
                                    .addQueryParameter(HINT_WORDS_PARAMETER_NAME, hintWords)
                                    .addQueryParameter(GUESSES_COUNT_PARAMETER_NAME, String.valueOf(hintNumber))
                                    .build();
                            break;
                        case GUESSER:
                            final Hint currentHint = gameInstanceData.getCurrentHint();
                            UIElements.printBoard(gameInstanceData, GameRole.GUESSER);
                            System.out.println("The current hint: \"" + currentHint.getHintWords() + "\", " + currentHint.getNumber());
                            System.out.println("Number of hints left: " + gameInstanceData.getGuessesLeft());
                            System.out.println("Please enter the number under each card to select the word, or enter \"" + ClientConstants.QUIT_STRING + "\" to move to the next turn:");
                            int cardNumber;
                            boolean validInput = false;
                            while (!validInput && !quitGuessing) {
                                cardNumber = InputController.intMenuInputWithQuit(OtherUtils.makeSetFromOneToN(upperLimit));
                                if (cardNumber == ClientConstants.QUIT_NUM) {
                                    finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_URL)).newBuilder().
                                            addQueryParameter(CARD_NUMBER_PARAMETER_NAME, ClientConstants.QUIT_STRING)
                                            .build();
                                    quitGuessing = true;
                                }
                                else {
                                    if (!gameInstanceData.isCardFound(cardNumber - 1)) {
                                        validInput = true;
                                    } else {
                                        System.out.println("This card has already been found. Select a different card.");
                                    }
                                    finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_URL)).newBuilder().
                                            addQueryParameter(CARD_NUMBER_PARAMETER_NAME, String.valueOf(cardNumber))
                                            .build();
                                }
                            }
                            break;
                    }
                    final Request req = new Request.Builder().get().url(finalUrl).build();
                    try {
                        String responseBody = ClientHttpClientUtils.sendGameRequest(req);
                        if (playerState.getRole().equals(GameRole.GUESSER)) {
                            if (!quitGuessing) {
                                final MoveEvent moveEvent = JSONUtils.fromJson(responseBody, MoveEvent.class);
                                checkMoveEvent(moveEvent, gameInstanceData.getGameStructure());
                            }
                            updateGameData();
                            gameData = getGameData();
                            if (gameData.getGameListingData().isGameActive()) {
                                if (!gameData.getGameInstanceData().getCurrentTurn().equals(currentTeam)) {
                                    System.out.println("It is now Team \"" + gameData.getGameInstanceData().getCurrentTurn().getName() + "\"'s turn!");
                                }
                            }
                        }
                        else {
                            System.out.println("Hint added successfully!");
                        }
                    } catch (NoPlayerStatusAtServerException e) {
                        System.out.println(e.getMessage());
                        setGameEnded();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    System.out.println("It is not the " + ((playerState.getRole().equals(GameRole.DEFINER)) ? "definers'" : "guessers'") + " turn yet or it has ended.");
                }
            }
            else {
                System.out.println("It is not your team's turn yet or it has ended. Current team: \"" + currentTeam.getName() + "\"");
            }
        }
        else {
            System.out.println("The game isn't active yet. Please wait until all players have connected.");
        }
    }

    @Override
    protected void printGameRoomMenu() {
        // TODO add chat, differ between definer and guesser
        System.out.println("Select an option:");
        System.out.println("(1) Retrieve current game status");
        System.out.println("(2) Perform a move");
    }

    @Override
    protected void gameRoomMenuSelection() {
        InputController.gameRoomMenuSelection(this);
    }

    private void checkMoveEvent(final MoveEvent moveEvent, final GameStructure gameStructure) {
        final boolean isBlackCard = moveEvent.isBlackCard();
        final boolean isNeutralCard = moveEvent.isNeutralCard();
        final boolean hasGameEnded = moveEvent.isHasGameEnded();
        final String cardTeam = moveEvent.getCardTeam().getName();
        final String selectingTeam = moveEvent.getSelectingTeam().getName();
        final int teamCount = gameStructure.getTeams().size();
        if (isNeutralCard) {
            System.out.println("That card was a neutral word, so no points were given to anyone.");
        }
        else if (isBlackCard) {
            System.out.println("That card was a black word!");
            teamLostMessage(moveEvent, teamCount);
            if (moveEvent.getTeamsThatLeftPlay().size() > 1) {
                otherTeamDefault(moveEvent, moveEvent.getOtherTeamName(selectingTeam), teamCount);
            }
            setGameEnded();
        }
        else {
            final boolean wasItMyCard = cardTeam.equals(getPlayerState().getTeam());
            System.out.println("That card belonged to " + ((wasItMyCard) ? "to your team, and gave you one point!" : ("to Team \"" + cardTeam + "\", and gave them one point...")));
            if (moveEvent.getTeamsThatLeftPlay().containsKey(cardTeam)) {
                if (wasItMyCard) {
                    teamWonMessage(moveEvent, teamCount);
                    setGameEnded();
                    if (moveEvent.getTeamsThatLeftPlay().size() > 1) {
                        otherTeamDefault(moveEvent, moveEvent.getOtherTeamName(selectingTeam), teamCount);
                    }
                }
                else {
                    otherTeamWon(moveEvent, cardTeam, teamCount);
                    if (moveEvent.getTeamsThatLeftPlay().containsKey(selectingTeam)) {
                        teamDefaultMessage(moveEvent, teamCount);
                    }
                }
            }
        }
        if (hasGameEnded) {
            System.out.println("The game has ended.");
            setGameEnded();
        }
    }

    private void teamWonMessage(final MoveEvent moveEvent, final int teamCount) {
        final TeamLeftEntry myTeamEntry = moveEvent.getTeamThatLeftPlay(getPlayerState().getTeam());
        System.out.println("Your team has won! In place: " + myTeamEntry.getWinPosition() + " out of " + teamCount);
    }

    private void teamLostMessage(final MoveEvent moveEvent, final int teamCount) {
        final TeamLeftEntry myTeamEntry = moveEvent.getTeamThatLeftPlay(getPlayerState().getTeam());
        System.out.println("Your team has lost... In place: " + myTeamEntry.getWinPosition() + " out of " + teamCount);
    }

    private void otherTeamWon(final MoveEvent moveEvent, final String teamName, final int teamCount) {
        final TeamLeftEntry otherTeamEntry = moveEvent.getTeamThatLeftPlay(teamName);
        System.out.println("Team \"" + teamName + "\" has won! In place: " + otherTeamEntry.getWinPosition() + " out of " + teamCount);
    }

    private void otherTeamDefault(final MoveEvent moveEvent, final String teamName, final int teamCount) {
        final TeamLeftEntry otherTeamEntry = moveEvent.getTeamThatLeftPlay(teamName);
        System.out.println("Team \"" + teamName + "\" has ended play by default. In place: " + otherTeamEntry.getWinPosition() + " out of " + teamCount);
    }

    private void teamDefaultMessage(final MoveEvent moveEvent, final int teamCount) {
        final TeamLeftEntry myTeamEntry = moveEvent.getTeamThatLeftPlay(getPlayerState().getTeam());
        System.out.println("Your team has ended play by default. In place: " + myTeamEntry.getWinPosition() + " out of " + teamCount);
    }
}
