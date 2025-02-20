package game.instance;

import game.structure.Team;

public class WordCard {
    private final String word;
    private final Team team;
    private final boolean isBlackWord;
    private boolean found = false;

    public WordCard(String word, Team team, boolean isBlackWord) {
        this.word = word;
        this.team = team;
        this.isBlackWord = isBlackWord;
    }

    public String getWord() {
        return word;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isBlackWord() { return isBlackWord; }

    public boolean isFound() {
        return found;
    }

    public void setFound() {
        this.found = true;
    }
}
