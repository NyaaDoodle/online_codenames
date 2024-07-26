package game.room;

import game.instance.GameInstanceData;
import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientGameRoom {
    private GameListingData gameListingData;
    private GameInstanceData gameInstanceData;
    private PlayerState playerState;
    private boolean gameEnded = false;

    public ClientGameRoom(@NotNull final GameListingData gameListingData) {
        this.gameListingData = gameListingData;
        this.gameInstanceData = null;
    }

    public void goToGameRoom() {
        while (!gameEnded) {
            hasGameEnded();
        }
    }

    @NotNull
    private GameListingData getGameListingData() {
        return gameListingData;
    }

    @Nullable
    private GameInstanceData getGameInstanceData() {
        return gameInstanceData;
    }

    private void updateGameData() {
        // TODO fetch game data from GET game servlet request

    }

    @NotNull
    private String getGameName() {
        return gameListingData.getName();
    }

    private boolean hasGameEnded() {
        // TODO that's not what it's supposed to do.........
        gameEnded = true;
        return true;
    }

    private static void gameRoomGreeter(final String gameName) {
        System.out.println("Successfully joined game \"" + gameName +"\"!");
    }

    private static void printGameRoomMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Retrieve current game status");
        System.out.println("(2) Perform a move");
    }

}
