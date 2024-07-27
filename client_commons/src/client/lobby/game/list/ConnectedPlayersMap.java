package client.lobby.game.list;

import client.utils.constants.ClientConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectedPlayersMap {
    private Map<String, Integer> connectedDefinersPerTeam = new HashMap<>();
    private Map<String, Integer> connectedGuessersPerTeam = new HashMap<>();

    public Map<String, Integer> getConnectedDefinersPerTeam() {
        return connectedDefinersPerTeam;
    }

    public void setConnectedDefinersPerTeam(Map<String, Integer> connectedDefinersPerTeam) {
        this.connectedDefinersPerTeam = connectedDefinersPerTeam;
    }

    public Map<String, Integer> getConnectedGuessersPerTeam() {
        return connectedGuessersPerTeam;
    }

    public void setConnectedGuessersPerTeam(Map<String, Integer> connectedGuessersPerTeam) {
        this.connectedGuessersPerTeam = connectedGuessersPerTeam;
    }

    public int getConnectedDefinersByTeam(final String teamName) {
        return connectedDefinersPerTeam.getOrDefault(teamName, ClientConstants.ERROR_NUM);
    }

    public int getConnectedGuessersByTeam(final String teamName) {
        return connectedGuessersPerTeam.getOrDefault(teamName, ClientConstants.ERROR_NUM);
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

    public void incrementConnectedDefinersByTeam(final String teamName) {
        connectedDefinersPerTeam.replace(teamName, getConnectedDefinersByTeam(teamName) + 1);
    }

    public void incrementConnectedGuessersByTeam(final String teamName) {
        connectedGuessersPerTeam.replace(teamName, getConnectedGuessersByTeam(teamName) + 1);
    }

}
