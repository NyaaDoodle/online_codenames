package lobby.game.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameList {
    private List<GameListingData> gameList = new ArrayList<>();
    private Map<String, GameListingData> gameMap = new HashMap<>();

    public List<GameListingData> getGameList() {
        return gameList;
    }

    public Map<String, GameListingData> getGameMap() { return gameMap; }

    public int getGameAmount() {
        return gameList.size();
    }

    public GameListingData getGameListing(final String gameName) { return gameMap.get(gameName); }

    public void setGameList(List<GameListingData> gameList) {
        this.gameList = gameList;
    }

    public void setGameMap(Map<String, GameListingData> gameMap) { this.gameMap = gameMap; }
}
