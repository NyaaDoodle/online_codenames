package lobby.game.list;

import game.structure.Team;

import java.util.*;

public class GameList {
    private final List<GameListingData> gameList;
    private final Map<String, GameListingData> gameMap = new HashMap<>();

    public GameList(final List<GameListingData> gameList) {
        this.gameList = gameList;
        gameList.forEach(gameListingData -> gameMap.put(gameListingData.getName(), gameListingData));
    }

    public List<GameListingData> getGameList() { return gameList; }

    public int getGameAmount() {
        return gameList.size();
    }

    public GameListingData getGameListing(final String gameName) {
        return gameMap.get(gameName);
    }
}
