package game.room;

import client.game.room.ClientGameRoom;
import client.game.room.PlayerState;
import input.InputController;
import client.lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;

public class GameRoom extends ClientGameRoom {
    public GameRoom(@NotNull GameListingData gameListingData, @NotNull PlayerState playerState) {
        super(gameListingData, playerState);
    }

    public void makeMove() {

    }

    @Override
    protected void printGameRoomMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Retrieve current game status");
        System.out.println("(2) Perform a move");
    }

    @Override
    protected void gameRoomMenuSelection() {
        InputController.gameRoomMenuSelection();
    }
}
