package game.room;

import client.game.instance.GameRole;
import client.game.room.ClientGameRoom;
import client.game.room.PlayerState;
import client.lobby.game.list.GameListingData;
import input.InputController;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.constants.Constants;
import utils.http.HttpClientUtils;

public class GameRoom extends ClientGameRoom {

    public GameRoom(@NotNull final GameListingData gameListingData, @NotNull final PlayerState playerState) {
        super(gameListingData, playerState);
    }

    @Override
    protected void printGameRoomMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Retrieve current game status");
        System.out.println("(2) Exit game");
    }

    @Override
    protected void gameRoomMenuSelection() {
        InputController.gameRoomMenuSelection(this);
    }

    public void exitGame() {
        final String finalUrl = Constants.BASE_URL + Constants.EXIT_GAME_RESOURCE_URI;
        Request req = new Request.Builder().get().url(finalUrl).build();
        try {
            HttpClientUtils.sendRequestSync(req);
            setGameEnded();
        } catch (Exception e) {
            UIElements.unexpectedExceptionMessage(e);
        }
    }
}
