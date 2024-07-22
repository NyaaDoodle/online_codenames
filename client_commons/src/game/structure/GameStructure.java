package game.structure;

import java.util.*;

public class GameStructure {
    private String name;
    private Board board;
    private List<Team> teams = new ArrayList<>();
    private Set<String> words = new HashSet<>();
    private String dictionaryFileName;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void setWords(Set<String> words) {
        this.words = words;
    }

    public void setDictionaryFileName(String dictionaryFileName) {
        this.dictionaryFileName = dictionaryFileName;
    }
}
