package game.instance;

import game.instance.data.WordCardData;
import game.structure.GameStructure;
import game.structure.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameInstance {
    private final static int STARTING_TEAM_SCORE = 0;

    private final GameStructure gameStructure;
    private final GameWordCards wordCards;
    private final Map<String, Integer> teamNameToScore = new HashMap<>();
    private final TurnOrder turnOrder;
    private Hint currentHint;
    private boolean hasGameEnded = false;
    private final WinOrder winOrder;

    public GameInstance(final GameStructure gameStructure, final LinkedList<Team> turnOrder) {
        this.gameStructure = gameStructure;
        this.wordCards = new GameWordCards(gameStructure);
        gameStructure.getTeams().forEach((team) -> teamNameToScore.put(team.getName(), STARTING_TEAM_SCORE));
        this.currentHint = null;
        this.turnOrder = new TurnOrder(turnOrder);
        this.winOrder = new WinOrder(gameStructure.getTeams().size());
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public Hint getCurrentHint() {
        return currentHint;
    }

    public void setCurrentHint(final Hint hint) {
        this.currentHint = hint;
    }

    public List<WordCardData> getWordCards() {
        return wordCards.getWordCardList();
    }

    public Map<String, Integer> getTeamNameToScore() {
        return Collections.unmodifiableMap(teamNameToScore);
    }

    public TurnOrder getTurnOrder() {
        return turnOrder;
    }

    public boolean hasGameEnded() {
        return hasGameEnded;
    }

    public void moveToNextTurn() {
        turnOrder.moveToNextTurn();
    }

    public MoveEvent makeMove(final int wordIndex) {
        final WordCardData selectedWord = wordCards.getWordCardData(wordIndex);
        final Team cardTeam = selectedWord.getTeam();
        final Team selectingTeam = turnOrder.getCurrentTurn();
        boolean isNeutralCard = false;
        boolean isBlackCard = false;
        wordCards.setFoundCard(wordIndex);
        if (gameStructure.getTeams().contains(cardTeam) && isTeamInPlay(cardTeam)) {
            addPointToTeam(cardTeam);
            checkIfTeamWon(cardTeam);
        }
        else if (selectedWord.isBlackWord()) {
            removeLosingTeamFromPlay(selectingTeam, EndPlayCause.BLACK_CARD);
            isBlackCard = true;
        }
        else {
            isNeutralCard = true;
        }
        return new MoveEvent(selectedWord.getWord(), wordIndex, cardTeam, selectingTeam, isNeutralCard, isBlackCard);
    }

    public List<Pair<Team, EndPlayCause>> getWinOrder() {
        return winOrder.getWinOrder();
    }

    private void removeWinningTeamFromPlay(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause) {
        winOrder.addWinningTeam(team, endPlayCause);
        turnOrder.removeTeam(team);
        checkIfOneTeamLeft();
    }

    private void removeLosingTeamFromPlay(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause) {
        winOrder.addLosingTeam(team, endPlayCause);
        turnOrder.removeTeam(team);
        checkIfOneTeamLeft();
    }

    private boolean isTeamInPlay(final Team team) {
        return turnOrder.isTeamInPlay(team);
    }

    private void addPointToTeam(final Team team) {
        teamNameToScore.put(team.getName(), teamNameToScore.get(team.getName()) + 1);
    }

    private void checkIfTeamWon(final Team team) {
        if (teamNameToScore.get(team.getName()).equals(team.getCardCount())) {
            removeWinningTeamFromPlay(team, EndPlayCause.FOUND_ALL_MY_CARDS);
        }
    }

    private void checkIfOneTeamLeft() {
        if (turnOrder.isOneTeamLeft()) {
            assert turnOrder.getCurrentTurn() != null;
            removeLosingTeamFromPlay(turnOrder.getCurrentTurn(), EndPlayCause.LAST_TEAM_LEFT);
            hasGameEnded = true;
        }
    }
}
