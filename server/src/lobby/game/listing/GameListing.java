package lobby.game.listing;

import game.instance.GameInstance;
import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;
import java.util.Map;

public class GameListing {
    private final GameStructure gameStructure;
    private final String name;
    private ListingState state;
    private ConnectedPlayersMap connectedPlayers;

    public GameListing(GameStructure gameStructure) {
        this.gameStructure = gameStructure;
        this.name = gameStructure.getName();
        this.state = ListingState.PENDING;
        this.connectedPlayers = new ConnectedPlayersMap(gameStructure.getTeams());
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public String getName() { return name; }

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
        if (this.state.equals(ListingState.PENDING)) {
            this.state = state;
        }
    }

    public void incrementConnectedDefiners() {
        if (this.state.equals(ListingState.PENDING)) {
            connectedDefiners++;
        }
    }

    public void incrementConnectedGuessers() {
        if (this.state.equals(ListingState.PENDING)) {
            connectedGuessers++;
        }
    }
}
