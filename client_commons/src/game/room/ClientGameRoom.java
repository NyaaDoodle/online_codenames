package game.room;

import game.data.GameData;
import lobby.game.list.GameListingData;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import ui.ClientUIElements;
import utils.constants.ClientConstants;
import utils.http.ClientHttpClientUtils;
import utils.json.ClientJSONUtils;

public abstract class ClientGameRoom {
    private GameData gameData;
    private final PlayerState playerState;
    private boolean gameEnded = false;

    public ClientGameRoom(@NotNull final GameListingData gameListingData, @NotNull PlayerState playerState) {
        this.gameData = new GameData(gameListingData, null);
        this.playerState = playerState;
    }

    public GameData getGameData() {
        return gameData;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean hasGameEnded() {
        return gameEnded;
    }

    public void setGameEnded() {
        this.gameEnded = true;
    }

    public void goToGameRoom() {
        gameRoomGreeter(playerState.getGame());
        while (!gameEnded) {
            printGameRoomMenu();
            gameRoomMenuSelection();
        }
    }

    public void updateGameData() {
        // TODO fetch game data from GET game servlet request
        final String finalUrl = ClientConstants.BASE_URL + ClientConstants.GAME_RESOURCE_URI;
        final Request req = new Request.Builder().get().url(finalUrl).build();
        try {
            final String jsonBody = ClientHttpClientUtils.sendRequestSync(req);
            gameData = ClientJSONUtils.fromJson(jsonBody, GameData.class);
        } catch (Exception e) {
            ClientUIElements.unexpectedExceptionMessage(e);
        }
    }

    private static void gameRoomGreeter(final String gameName) {
        System.out.println("Successfully joined game \"" + gameName +"\"!");
    }

    abstract protected void printGameRoomMenu();
    abstract protected void gameRoomMenuSelection();
}
