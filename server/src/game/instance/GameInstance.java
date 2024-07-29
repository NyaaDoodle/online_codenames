package game.instance;

import game.instance.data.WordCardData;
import game.structure.GameStructure;
import game.structure.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameInstance {
    private final static int STARTING_TEAM_SCORE = 0;
    private final static int STARTING_GUESSES_COUNT = 0;

    private final GameStructure gameStructure;
    private final GameWordCards wordCards;
    private final Map<String, Integer> teamNameToScore = new HashMap<>();
    private final TurnOrder turnOrder;
    private Hint currentHint;
    private boolean hasGameEnded = false;
    private int guessesLeft = STARTING_GUESSES_COUNT;
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
        setGuessesLeft(hint.getNumber());
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

    public int getGuessesLeft() {
        return guessesLeft;
    }

    public void setGuessesLeft(final int guessesCount) {
        this.guessesLeft = guessesCount;
    }

    public boolean isCardFound(final int cardIndex) {
        return wordCards.getWordCardData(cardIndex).isFound();
    }

    public MoveEvent makeMove(final int wordIndex) {
        if (guessesLeft > 0 && !isCardFound(wordIndex)) {
            final WordCardData selectedWord = wordCards.getWordCardData(wordIndex);
            final Team cardTeam = selectedWord.getTeam();
            final Team selectingTeam = turnOrder.getCurrentTurn();
            final MoveEvent moveEvent = new MoveEvent(selectedWord.getWord(), wordIndex, cardTeam, selectingTeam);
            wordCards.setFoundCard(wordIndex);
            if (gameStructure.getTeams().contains(cardTeam) && isTeamInPlay(cardTeam)) {
                addPointToTeam(cardTeam);
                checkIfTeamWon(cardTeam, moveEvent);
            } else if (selectedWord.isBlackWord()) {
                removeLosingTeamFromPlay(selectingTeam, EndPlayCause.BLACK_CARD, moveEvent);
                moveEvent.setBlackCard(true);
            } else {
                moveEvent.setNeutralCard(true);
            }
            if (!hasGameEnded) {
                if (isTeamInPlay(selectingTeam)) {
                    guessesLeft--;
                    if (guessesLeft < 1) {
                        endTurn();
                    }
                } else {
                    endTurn();
                }
            }
            return moveEvent;
        }
        else {
            return null;
        }
    }

    public void endTurn() {
        currentHint = null;
        guessesLeft = STARTING_GUESSES_COUNT;
        turnOrder.moveToNextDefinersTurn();
    }

    public List<Pair<Team, EndPlayCause>> getWinOrder() {
        return winOrder.getWinOrder();
    }

    private void removeWinningTeamFromPlay(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause, @NotNull final MoveEvent moveEvent) {
        winOrder.addWinningTeam(team, endPlayCause);
        removeTeamAndCheckForLastTeam(team, endPlayCause, moveEvent);
    }

    private void removeLosingTeamFromPlay(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause, @NotNull final MoveEvent moveEvent) {
        winOrder.addLosingTeam(team, endPlayCause);
        removeTeamAndCheckForLastTeam(team, endPlayCause, moveEvent);
    }

    private void removeTeamAndCheckForLastTeam(@NotNull Team team, @NotNull EndPlayCause endPlayCause, @NotNull MoveEvent moveEvent) {
        turnOrder.removeTeam(team);
        moveEvent.addLeavingTeam(team, endPlayCause, winOrder.getWinNumberOfTeam(team));
        if (turnOrder.isOneTeamLeft()) {
            endTurn();
            final Team lastTeam = turnOrder.getCurrentTurn();
            assert lastTeam != null;
            winOrder.addLosingTeam(lastTeam, EndPlayCause.LAST_TEAM_LEFT);
            turnOrder.removeTeam(lastTeam);
            moveEvent.addLeavingTeam(lastTeam, EndPlayCause.LAST_TEAM_LEFT, winOrder.getWinNumberOfTeam(lastTeam));
            moveEvent.setGameEnded();
            hasGameEnded = true;
        }
    }

    private boolean isTeamInPlay(@NotNull final Team team) {
        return turnOrder.isTeamInPlay(team);
    }

    private void addPointToTeam(@NotNull final Team team) {
        teamNameToScore.put(team.getName(), teamNameToScore.get(team.getName()) + 1);
    }

    private void checkIfTeamWon(@NotNull final Team team, @NotNull final MoveEvent moveEvent) {
        if (teamNameToScore.get(team.getName()).equals(team.getCardCount())) {
            removeWinningTeamFromPlay(team, EndPlayCause.FOUND_ALL_MY_CARDS, moveEvent);
        }
    }
}
