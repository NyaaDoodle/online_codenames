package lobby.game.list;

import game.structure.GameStructure;
import game.structure.Team;

public class GameListing {
    private final GameStructure gameStructure;
    private final String name;
    private ListingState state;
    private final ConnectedPlayersMap connectedPlayers;

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

    public ConnectedPlayersMap getConnectedPlayers() {
        return connectedPlayers;
    }

    public int getConnectedDefinersByTeam(final Team team) {
        return connectedPlayers.getConnectedDefinersByTeam(team);
    }

    public int getConnectedGuessersByTeam(final Team team) {
        return connectedPlayers.getConnectedGuessersByTeam(team);
    }

    public int getTotalConnectedPlayers() {
        return connectedPlayers.getTotalConnectedPlayers();
    }

    public synchronized void incrementConnectedDefinersByTeam(final Team team) {
        if (state.equals(ListingState.PENDING)) {
            if (getConnectedDefinersByTeam(team) < team.getDefinersCount()) {
                connectedPlayers.incrementConnectedDefinersByTeam(team);
            }
        }
        checkIfListingFull();
    }

    public synchronized void incrementConnectedGuessersByTeam(final Team team) {
        if (state.equals(ListingState.PENDING)) {
            if (getConnectedGuessersByTeam(team) < team.getGuessersCount()) {
                connectedPlayers.incrementConnectedGuessersByTeam(team);
            }
        }
        checkIfListingFull();
    }

    private void setGameAsActive() {
        if (state.equals(ListingState.PENDING)) {
            this.state = ListingState.ACTIVE;
        }
    }

    private void checkIfListingFull() {
        int expectedTotalPlayers = gameStructure.getTeams().stream()
                .mapToInt(team -> team.getDefinersCount() + team.getGuessersCount()).sum();
        if (connectedPlayers.getTotalConnectedPlayers() == expectedTotalPlayers) {
            setGameAsActive();
        }
    }
}
