package game.data;

import lobby.game.list.GameListingData;

public class GameData {
    private final GameListingData gameListingData;
    private final GameInstanceData gameInstanceData;

    public GameData(GameListingData gameListingData, GameInstanceData gameInstanceData) {
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
