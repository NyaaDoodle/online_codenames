package application;

import client.game.instance.GameRole;
import client.game.room.PlayerState;
import client.game.structure.Team;
import client.lobby.game.list.GameListingData;
import game.room.GameRoom;
import input.InputController;
import client.lobby.LobbyController;
import client.lobby.game.list.GameList;
import okhttp3.Request;
import okhttp3.RequestBody;
import ui.UIElements;
import utils.constants.Constants;
import utils.http.HttpClientUtils;
import utils.json.JSONUtils;

public class GameSpectator {
    private static final GameRole ADMIN_GAME_ROLE = GameRole.DEFINER;
    private static final Team ADMIN_TEAM = new Team();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON_TYPE = "application/json";
    public static void selectGame() {
        selectActiveGameMessage();
        UIElements.goBackOptionMessage();
        GameList activeGameList;
        try {
            activeGameList = LobbyController.getGameListOnlyActive();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        UIElements.printGameList(activeGameList);
        if (activeGameList.getGameAmount() > 0) {
            final GameListingData selectedGame = InputController.spectateGameSelection(activeGameList);
            if (selectedGame != null) {
                final PlayerState adminPlayerState = new PlayerState(selectedGame.getName(), ADMIN_TEAM.getName(), ADMIN_GAME_ROLE);
                try {
                    sendSelectionRequest(adminPlayerState);
                    final GameRoom gameRoom = new GameRoom(selectedGame, adminPlayerState);
                    gameRoom.goToGameRoom();
                } catch (Exception e) {
                    UIElements.unexpectedExceptionMessage(e);
                }
            }
        }
    }

    private static void selectActiveGameMessage() {
        System.out.println("Select one of the following active games, according to the number on top of each game listing:");
    }

    private static void sendSelectionRequest(final PlayerState playerState) throws Exception {
        final String json = JSONUtils.toJson(playerState);
        final String finalUrl = Constants.BASE_URL + Constants.JOIN_GAME_RESOURCE_URI;
        final RequestBody body = RequestBody.create(json.getBytes());
        Request req = new Request.Builder().post(body).url(finalUrl).addHeader(CONTENT_TYPE, JSON_TYPE).build();
        HttpClientUtils.sendJoinGameRequest(req);
    }
}
