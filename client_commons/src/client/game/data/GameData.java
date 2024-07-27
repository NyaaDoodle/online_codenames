package client.game.data;

import client.game.instance.data.GameInstanceData;
import client.lobby.game.list.GameListingData;

public class GameData {
    private final GameListingData gameListingData;
    private final GameInstanceData gameInstanceData;

    public GameData(final GameListingData gameListingData, final GameInstanceData gameInstanceData) {
        this.gameListingData = gameListingData;
        this.gameInstanceData = gameInstanceData;
    }

    public GameListingData getGameListingData() {
        return gameListingData;
    }

    public GameInstanceData getGameInstanceData() {
        return gameInstanceData;
    }
}
