package game.instance;

import game.structure.Team;

import java.util.HashMap;
import java.util.Map;

public class MoveEvent {
    private final String word;
    private final int index;
    private final Team cardTeam;
    private final Team selectingTeam;

    private boolean isNeutralCard;
    private boolean isBlackCard;

    private final Map<String, TeamLeftEntry> teamsThatLeftPlay;

    private boolean hasGameEnded;

    public MoveEvent(final String word, final int index, final Team cardTeam, final Team selectingTeam) {
        this.word = word;
        this.index = index;
        this.cardTeam = cardTeam;
        this.selectingTeam = selectingTeam;
        isNeutralCard = false;
        isBlackCard = false;
        teamsThatLeftPlay = new HashMap<>();
        hasGameEnded = false;
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

    public void setNeutralCard(boolean neutralCard) {
        isNeutralCard = neutralCard;
    }

    public boolean isBlackCard() {
        return isBlackCard;
    }

    public void setBlackCard(boolean blackCard) {
        isBlackCard = blackCard;
    }

    public Map<String, TeamLeftEntry> getTeamsThatLeftPlay() {
        return teamsThatLeftPlay;
    }

    public void addLeavingTeam(final Team team, final EndPlayCause endPlayCause, final int winPosition) {
        teamsThatLeftPlay.put(team.getName(), new TeamLeftEntry(team, endPlayCause, winPosition));
    }

    public boolean hasGameEnded() {
        return hasGameEnded;
    }

    public void setGameEnded() {
        this.hasGameEnded = true;
    }
}
