package lobby.game.list;

import game.structure.Team;
import utils.constants.ClientConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectedPlayersMap {
    private Map<Team, Integer> connectedDefinersPerTeam = new HashMap<>();
    private Map<Team, Integer> connectedGuessersPerTeam = new HashMap<>();

    public Map<Team, Integer> getConnectedDefinersPerTeam() {
        return connectedDefinersPerTeam;
    }

    public void setConnectedDefinersPerTeam(Map<Team, Integer> connectedDefinersPerTeam) {
        this.connectedDefinersPerTeam = connectedDefinersPerTeam;
    }

    public Map<Team, Integer> getConnectedGuessersPerTeam() {
        return connectedGuessersPerTeam;
    }

    public void setConnectedGuessersPerTeam(Map<Team, Integer> connectedGuessersPerTeam) {
        this.connectedGuessersPerTeam = connectedGuessersPerTeam;
    }

    public int getConnectedDefinersByTeam(final Team team) {
        return connectedDefinersPerTeam.getOrDefault(team, ClientConstants.ERROR_NUM);
    }

    public int getConnectedGuessersByTeam(final Team team) {
        return connectedGuessersPerTeam.getOrDefault(team, ClientConstants.ERROR_NUM);
    }

    public int getTotalConnectedDefiners() {
        return connectedDefinersPerTeam.keySet().stream().mapToInt(this::getConnectedDefinersByTeam).sum();
    }

    public int getTotalConnectedGuessers() {
        return connectedGuessersPerTeam.keySet().stream().mapToInt(this::getConnectedGuessersByTeam).sum();
    }

    public int getTotalConnectedPlayers() {
        return getTotalConnectedDefiners() + getTotalConnectedGuessers();
    }

    public void incrementConnectedDefinersByTeam(final Team team) {
        connectedDefinersPerTeam.replace(team, getConnectedDefinersByTeam(team) + 1);
    }

    public void incrementConnectedGuessersByTeam(final Team team) {
        connectedGuessersPerTeam.replace(team, getConnectedGuessersByTeam(team) + 1);
    }

}
