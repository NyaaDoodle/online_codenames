package lobby.game.list;

import java.util.ArrayList;
import java.util.List;

public class GameList {
    private List<GameListingData> gameList = new ArrayList<>();

    public List<GameListingData> getGameList() {
        return gameList;
    }

    public int getGameAmount() {
        return gameList.size();
    }

    public void setGameList(List<GameListingData> gameList) {
        this.gameList = gameList;
    }
}
