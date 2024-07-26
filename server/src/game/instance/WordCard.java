package game.instance;

import game.structure.Team;

public class WordCard {
    private final String word;
    private final Team team;
    private final boolean isBlackWord;
    private boolean found = false;
    private int index;
    private boolean indexSet = false;

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

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (!indexSet) {
            this.index = index;
            indexSet = true;
        }
    }
}
