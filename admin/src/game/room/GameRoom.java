package game.room;

import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;

public class GameRoom extends ClientGameRoom {
    public GameRoom(@NotNull GameListingData gameListingData) {
        super(gameListingData);
    }
}
