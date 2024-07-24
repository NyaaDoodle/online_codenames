package game.structure;

import java.util.*;

public class GameStructure {
    private final String name;
    private final Board board;
    private final List<Team> teams;
    private final Map<String, Team> teamMap = new HashMap<>();
    private final Set<String> words;
    private final String dictionaryFileName;

    public GameStructure(String name, Board board, List<Team> teams, Set<String> words, String dictionaryFileName) {
        this.name = name;
        this.board = board;
        this.teams = teams;
        teams.forEach(team -> teamMap.put(team.getName(), team));
        this.words = words;
        this.dictionaryFileName = dictionaryFileName;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }

    public String getDictionaryFileName() {
        return dictionaryFileName;
    }

    public Team getTeam(final String teamName) {
        return teamMap.get(teamName);
    }
}
