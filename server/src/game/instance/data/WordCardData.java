package game.instance.data;

import game.instance.WordCard;
import game.structure.Team;

public class WordCardData {
    private final String word;
    private final Team team;
    private final boolean isBlackWord;
    private final boolean found;

    public WordCardData(final WordCard wordCard) {
        word = wordCard.getWord();
        team = wordCard.getTeam();
        isBlackWord = wordCard.isBlackWord();
        found = wordCard.isFound();
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
