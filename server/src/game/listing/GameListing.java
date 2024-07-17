package game.listing;

import game.structure.GameStructure;

public class GameListing {
    private final GameStructure gameStructure;
    private ListingState state;
    private int connectedDefiners;
    private int connectedGuessers;

    public GameListing(GameStructure gameStructure) {
        this.gameStructure = gameStructure;
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public ListingState getState() {
        return state;
    }

    public int getConnectedDefiners() {
        return connectedDefiners;
    }

    public int getConnectedGuessers() {
        return connectedGuessers;
    }

    public void setState(ListingState state) {
        this.state = state;
    }

    public void incrementConnectedDefiners() {
        connectedDefiners++;
    }

    public void incrementConnectedGuessers() {
        connectedGuessers++;
    }
}
