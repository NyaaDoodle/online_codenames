package client.game.instance;

import client.game.structure.Team;

import java.util.Map;
import java.util.stream.Collectors;

public class MoveEvent {
    private String word;
    private int index;
    private Team cardTeam;
    private Team selectingTeam;

    private boolean isNeutralCard;
    private boolean isBlackCard;

    private Map<String, TeamLeftEntry> teamsThatLeftPlay;

    private boolean hasGameEnded;

    public MoveEvent(String word, int index, Team cardTeam, Team selectingTeam, boolean isNeutralCard, boolean isBlackCard, Map<String, TeamLeftEntry> teamsThatLeftPlay, boolean hasGameEnded) {
        this.word = word;
        this.index = index;
        this.cardTeam = cardTeam;
        this.selectingTeam = selectingTeam;
        this.isNeutralCard = isNeutralCard;
        this.isBlackCard = isBlackCard;
        this.teamsThatLeftPlay = teamsThatLeftPlay;
        this.hasGameEnded = hasGameEnded;
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

    public Map<String, TeamLeftEntry> getTeamsThatLeftPlay() {
        return teamsThatLeftPlay;
    }

    public void setTeamsThatLeftPlay(Map<String, TeamLeftEntry> teamsThatLeftPlay) {
        this.teamsThatLeftPlay = teamsThatLeftPlay;
    }

    public boolean isHasGameEnded() {
        return hasGameEnded;
    }

    public void setHasGameEnded(boolean hasGameEnded) {
        this.hasGameEnded = hasGameEnded;
    }

    public TeamLeftEntry getTeamThatLeftPlay(final String teamName) {
        return teamsThatLeftPlay.get(teamName);
    }

    public String getOtherTeamName(final String notThisTeamName) {
        if (teamsThatLeftPlay.size() > 1) {
            return teamsThatLeftPlay.keySet().stream().filter(name -> !name.equals(notThisTeamName)).collect(Collectors.toList()).get(0);
        }
        return null;
    }
}
