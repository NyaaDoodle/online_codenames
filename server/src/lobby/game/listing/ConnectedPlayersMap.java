package lobby.game.listing;

import game.structure.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConnectedPlayersMap {
    private static final int ERROR_NUM = -1;
    private final Map<Team, Integer> connectedDefinersPerTeam = new HashMap<>();
    private final Map<Team, Integer> connectedGuessersPerTeam = new HashMap<>();

    public ConnectedPlayersMap(final List<Team> teams) {
        teams.forEach(team -> {
            connectedDefinersPerTeam.put(team, 0);
            connectedGuessersPerTeam.put(team, 0);
        });
    }

    public int getConnectedDefinersByTeam(final Team team) {
        return connectedDefinersPerTeam.getOrDefault(team, ERROR_NUM);
    }

    public int getConnectedDefinersByTeam(final String teamName) {
        Team team = getTeam(teamName);
        return (team != null) ? getConnectedDefinersByTeam(team) : ERROR_NUM;
    }

    public int getConnectedGuessersByTeam(final Team team) {
        return connectedGuessersPerTeam.getOrDefault(team, ERROR_NUM);
    }

    public int getConnectedGuessersByTeam(final String teamName) {
        Team team = getTeam(teamName);
        return (team != null) ? getConnectedGuessersByTeam(team) : ERROR_NUM;
    }

    public int getTotalConnectedDefiners() {
        return connectedDefinersPerTeam.keySet().stream().mapToInt(this::getConnectedDefinersByTeam).sum();
    }

    public int getTotalConnectedGuessers() {
        return connectedGuessersPerTeam.keySet().stream().mapToInt(this::getConnectedGuessersByTeam).sum();
    }

    public int getTotalConnectedUsers() {
        return getTotalConnectedDefiners() + getTotalConnectedGuessers();
    }

    public void incrementConnectedDefinersByTeam(final Team team) {
        connectedDefinersPerTeam.replace(team, getConnectedDefinersByTeam(team) + 1);
    }

    public void incrementConnectedDefinersByTeam(final String teamName) {
        Team team = getTeam(teamName);
        if (team != null) {
            incrementConnectedDefinersByTeam(team);
        }
    }

    public void incrementConnectedGuessersByTeam(final Team team) {
        connectedGuessersPerTeam.replace(team, getConnectedGuessersByTeam(team) + 1);
    }

    public void incrementConnectedGuessersByTeam(final String teamName) {
        Team team = getTeam(teamName);
        if (team != null) {
            incrementConnectedGuessersByTeam(team);
        }
    }

    private Team getTeam(final String teamName) {
        Optional<Team> optional = connectedDefinersPerTeam.keySet().stream().filter(team -> team.getName().equals(teamName)).findAny();
        return optional.orElse(null);
    }
}
