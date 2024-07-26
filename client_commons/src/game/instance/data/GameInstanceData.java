package game.instance.data;

import game.instance.*;
import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;
import java.util.Map;

public class GameInstanceData {
    private final GameStructure gameStructure;
    private final List<WordCardData> wordCards;
    private final Map<String, Integer> teamNameToScore;
    private final TurnOrder turnOrder;
    private final Hint currentHint;
    private final boolean hasGameEnded;
    private final List<Pair<Team, EndPlayCause>> winOrder;

    public GameInstanceData(GameStructure gameStructure, List<WordCardData> wordCards, Map<String, Integer> teamNameToScore,
                            TurnOrder turnOrder, Hint currentHint, boolean hasGameEnded, List<Pair<Team, EndPlayCause>> winOrder) {
        this.gameStructure = gameStructure;
        this.wordCards = wordCards;
        this.teamNameToScore = teamNameToScore;
        this.turnOrder = turnOrder;
        this.currentHint = currentHint;
        this.hasGameEnded = hasGameEnded;
        this.winOrder = winOrder;
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public List<WordCardData> getWordCards() {
        return wordCards;
    }

    public Map<String, Integer> getTeamNameToScore() {
        return teamNameToScore;
    }

    public TurnOrder getTurnOrder() {
        return turnOrder;
    }

    public Hint getCurrentHint() {
        return currentHint;
    }

    public boolean isHasGameEnded() {
        return hasGameEnded;
    }

    public List<Pair<Team, EndPlayCause>> getWinOrder() {
        return winOrder;
    }

    public boolean isTeamInPlay(final Team team) {
        return turnOrder.isTeamInPlay(team);
    }

    public Team getCurrentTurn() {
        return turnOrder.getCurrentTurn();
    }

    public Team getNextTurn() {
        return turnOrder.getNextTurn();
    }

    public GameRole getCurrentRole() {
        return turnOrder.getCurrentRole();
    }

    public Integer getTeamScore(final String teamName) {
        return teamNameToScore.get(teamName);
    }
}
