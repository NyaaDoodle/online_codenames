package lobby.game.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameList {
    private final List<GameListingData> gameList;

    public GameList(List<GameListingData> gameList) {
        this.gameList = gameList;
    }

    public List<GameListingData> getGameList() { return gameList; }

    public int getGameAmount() {
        return gameList.size();
    }
}
