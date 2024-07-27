package client.game.instance;

import client.game.structure.Team;
import client.utils.constants.ClientConstants;

public class MoveEvent {
    private String word;
    private int index;
    private Team cardTeam;
    private Team selectingTeam;

    private boolean isNeutralCard;
    private boolean isBlackCard;

    private boolean didSelectingTeamLeavePlay;
    private EndPlayCause endPlayCause;
    private int winPosition;

    public MoveEvent(final String word, final int index, final Team cardTeam, final Team selectingTeam) {
        this.word = word;
        this.index = index;
        this.cardTeam = cardTeam;
        this.selectingTeam = selectingTeam;
        this.isNeutralCard = false;
        this.isBlackCard = false;
        didSelectingTeamLeavePlay = false;
        endPlayCause= null;
        winPosition = ClientConstants.ERROR_NUM;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Team getCardTeam() {
        return cardTeam;
    }

    public void setCardTeam(Team cardTeam) {
        this.cardTeam = cardTeam;
    }

    public Team getSelectingTeam() {
        return selectingTeam;
    }

    public void setSelectingTeam(Team selectingTeam) {
        this.selectingTeam = selectingTeam;
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

    public boolean isDidSelectingTeamLeavePlay() {
        return didSelectingTeamLeavePlay;
    }

    public void setDidSelectingTeamLeavePlay(boolean didSelectingTeamLeavePlay) {
        this.didSelectingTeamLeavePlay = didSelectingTeamLeavePlay;
    }

    public EndPlayCause getEndPlayCause() {
        return endPlayCause;
    }

    public void setEndPlayCause(EndPlayCause endPlayCause) {
        this.endPlayCause = endPlayCause;
    }

    public int getWinPosition() {
        return winPosition;
    }

    public void setWinPosition(int winPosition) {
        this.winPosition = winPosition;
    }

    public boolean wasItMyTeam() {
        return cardTeam.equals(selectingTeam);
    }
}
