package client.game.room;

import client.exceptions.NoPlayerStatusAtServerException;
import client.game.data.GameData;
import client.lobby.game.list.GameListingData;
import client.utils.constants.ClientConstants;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import client.ui.ClientUIElements;
import client.utils.http.ClientHttpClientUtils;
import client.utils.json.ClientJSONUtils;

public abstract class ClientGameRoom {
    private GameData gameData;
    private final PlayerState playerState;
    private boolean gameEnded = false;

    public ClientGameRoom(@NotNull final GameListingData gameListingData, @NotNull final PlayerState playerState) {
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
        leftGameRoomMessage();
    }

    public void updateGameData() {
        final String finalUrl = ClientConstants.BASE_URL + ClientConstants.GAME_DATA_RESOURCE_URI;
        final Request req = new Request.Builder().get().url(finalUrl).build();
        try {
            final String jsonBody = ClientHttpClientUtils.sendGameRequest(req);
            gameData = ClientJSONUtils.fromJson(jsonBody, GameData.class);
        } catch (NoPlayerStatusAtServerException e) {
            System.out.println(e.getMessage());
            setGameEnded();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void gameRoomGreeter(final String gameName) {
        System.out.println("Successfully joined game \"" + gameName +"\"!");
    }

    private static void leftGameRoomMessage() {
        System.out.println("You have left the game room.");
    }

    abstract protected void printGameRoomMenu();
    abstract protected void gameRoomMenuSelection();
}
