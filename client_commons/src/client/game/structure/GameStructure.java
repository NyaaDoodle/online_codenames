package client.game.structure;

import java.util.*;

public class GameStructure {
    private String name;
    private Board board;
    private List<Team> teams = new ArrayList<>();
    private Map<String, Team> teamMap = new HashMap<>();
    private Set<String> words = new HashSet<>();
    private String dictionaryFileName;

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public List<Team> getTeams() { return teams; }

    public Map<String, Team> getTeamMap() { return teamMap; }

    public Set<String> getWords() { return words; }

    public String getDictionaryFileName() {
        return dictionaryFileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void setTeamMap(Map<String, Team> teamMap) { this.teamMap = teamMap; }

    public void setWords(Set<String> words) { this.words = words; }

    public void setDictionaryFileName(String dictionaryFileName) {
        this.dictionaryFileName = dictionaryFileName;
    }

    public Team getTeam(final String teamName) {
        return teamMap.get(teamName);
    }
}
