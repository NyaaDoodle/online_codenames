package game.instance;

import game.structure.Team;

public class MoveEvent {
    private final String word;
    private final int index;
    private final Team cardTeam;
    private final Team selectingTeam;
    private final boolean isNeutralCard;
    private final boolean isBlackCard;

    public MoveEvent(final String word, final int index, final Team cardTeam,
                     final Team selectingTeam, final boolean isNeutralCard, final boolean isBlackCard) {
        this.word = word;
        this.index = index;
        this.cardTeam = cardTeam;
        this.selectingTeam = selectingTeam;
        this.isNeutralCard = isNeutralCard;
        this.isBlackCard = isBlackCard;
    }

    public String getWord() {
        return word;
    }

    public int getIndex() {
        return index;
    }

    public Team getCardTeam() {
        return cardTeam;
    }

    public Team getSelectingTeam() {
        return selectingTeam;
    }

    public boolean isNeutralCard() {
        return isNeutralCard;
    }

    public boolean isBlackCard() {
        return isBlackCard;
    }

    public boolean wasItMyTeam() {
        return cardTeam.equals(selectingTeam);
    }
}
