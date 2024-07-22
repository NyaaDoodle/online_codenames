package lobby;

import game.structure.GameStructure;
import lobby.game.list.GameList;
import lobby.game.list.GameListing;
import lobby.game.list.GameListingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LobbyManager {
    private final Map<String, GameListing> gameListings = new HashMap<>();

    public void addGame(final GameStructure newGame) {
        // Assuming the game structure has been checked for game name uniqueness
        GameListing newGameListing = new GameListing(newGame);
        gameListings.put(newGameListing.getName(), newGameListing);
    }

    public GameListingData getGameListing(final String gameName) {
        GameListing gameListing = gameListings.get(gameName);
        return (gameListing == null ? null : new GameListingData(gameListing));
    }

    public boolean doesGameAlreadyExist(final String gameName) {
        return gameListings.get(gameName) != null;
    }

    public GameList getGameList() {
        final List<GameListingData> gameList = gameListings.values().stream().map(GameListingData::new).collect(Collectors.toList());
        return new GameList(gameList);
    }
}
