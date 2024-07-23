package lobby.game.list;

import game.structure.Team;
import utils.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectedPlayersMap {
    private final Map<String, Integer> connectedDefinersPerTeam = new HashMap<>();
    private final Map<String, Integer> connectedGuessersPerTeam = new HashMap<>();

    public ConnectedPlayersMap(final List<Team> teams) {
        teams.forEach(team -> {
            connectedDefinersPerTeam.put(team.getName(), 0);
            connectedGuessersPerTeam.put(team.getName(), 0);
        });
    }

    public int getConnectedDefinersByTeam(final String teamName) {
        return connectedDefinersPerTeam.getOrDefault(teamName, Constants.ERROR_NUM);
    }

    public int getConnectedGuessersByTeam(final String teamName) {
        return connectedGuessersPerTeam.getOrDefault(teamName, Constants.ERROR_NUM);
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
