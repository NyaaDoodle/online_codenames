package utils;

import game.structure.Team;

import java.util.Collection;

public class FindUtils {
    public static Team getTeam(final String teamName, final Collection<Team> teams) {
        return teams.stream().filter(team -> team.getName().equals(teamName)).findAny().orElse(null);
    }
}
