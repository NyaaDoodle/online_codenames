package lobby.game.list;

import game.structure.GameStructure;
import game.structure.Team;
import utils.FindUtils;

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

    public int getConnectedDefinersByTeam(final String teamName) {
        return connectedPlayers.getConnectedDefinersByTeam(teamName);
    }

    public int getConnectedGuessersByTeam(final String teamName) {
        return connectedPlayers.getConnectedGuessersByTeam(teamName);
    }

    public int getTotalConnectedPlayers() {
        return connectedPlayers.getTotalConnectedPlayers();
    }

    public synchronized void incrementConnectedDefinersByTeam(final String teamName) {
        final Team team = FindUtils.getTeam(teamName, gameStructure.getTeams());
        if (team != null) {
            if (state.equals(ListingState.PENDING)) {
                if (getConnectedDefinersByTeam(teamName) < team.getDefinersCount()) {
                    connectedPlayers.incrementConnectedDefinersByTeam(teamName);
                }
            }
        }
        checkIfListingFull();
    }

    public synchronized void incrementConnectedGuessersByTeam(final String teamName) {
        final Team team = FindUtils.getTeam(teamName, gameStructure.getTeams());
        if (state.equals(ListingState.PENDING)) {
            if (getConnectedGuessersByTeam(teamName) < team.getGuessersCount()) {
                connectedPlayers.incrementConnectedGuessersByTeam(teamName);
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
