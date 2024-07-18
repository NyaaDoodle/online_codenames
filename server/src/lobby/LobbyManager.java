package lobby;

import game.structure.GameStructure;
import lobby.game.listing.GameListing;
import lobby.game.listing.GameListingData;

import java.util.HashMap;
import java.util.Map;

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
}
