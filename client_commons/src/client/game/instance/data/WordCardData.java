package client.game.instance.data;

import client.game.structure.Team;

public class WordCardData {
    private final String word;
    private final Team team;
    private final boolean isBlackWord;
    private final boolean found;

    public WordCardData(String word, Team team, boolean isBlackWord, boolean found) {
        this.word = word;
        this.team = team;
        this.isBlackWord = isBlackWord;
        this.found = found;
    }

    public String getWord() {
        return word;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isBlackWord() {
        return isBlackWord;
    }

    public boolean isFound() {
        return found;
    }
}
