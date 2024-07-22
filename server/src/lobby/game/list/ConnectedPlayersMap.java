package lobby.game.list;

import game.structure.Team;
import utils.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectedPlayersMap {
    private final Map<Team, Integer> connectedDefinersPerTeam = new HashMap<>();
    private final Map<Team, Integer> connectedGuessersPerTeam = new HashMap<>();

    public ConnectedPlayersMap(final List<Team> teams) {
        teams.forEach(team -> {
            connectedDefinersPerTeam.put(team, 0);
            connectedGuessersPerTeam.put(team, 0);
        });
    }

    public int getConnectedDefinersByTeam(final Team team) {
        return connectedDefinersPerTeam.getOrDefault(team, Constants.ERROR_NUM);
    }

    public int getConnectedGuessersByTeam(final Team team) {
        return connectedGuessersPerTeam.getOrDefault(team, Constants.ERROR_NUM);
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
