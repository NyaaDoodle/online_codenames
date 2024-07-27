package game.instance.data;

import game.instance.*;
import game.structure.GameStructure;
import game.structure.Team;
import lobby.game.join.GameRole;

import java.util.List;
import java.util.Map;

public class GameInstanceData {
    private final GameStructure gameStructure;
    private final List<WordCardData> wordCards;
    private final Map<String, Integer> teamNameToScore;
    private final TurnOrder turnOrder;
    private final Hint currentHint;
    private final boolean hasGameEnded;
    private final int guessesLeft;
    private final List<Pair<Team, EndPlayCause>> winOrder;

    public GameInstanceData(final GameInstance gameInstance) {
        gameStructure = gameInstance.getGameStructure();
        wordCards = gameInstance.getWordCards();
        teamNameToScore = gameInstance.getTeamNameToScore();
        turnOrder = gameInstance.getTurnOrder();
        currentHint = gameInstance.getCurrentHint();
        hasGameEnded = gameInstance.hasGameEnded();
        guessesLeft = gameInstance.getGuessesLeft();
        winOrder = gameInstance.getWinOrder();
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

    public int getGuessesLeft() {
        return guessesLeft;
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

    public void censorWordCards() {
        // Every card that hasn't been revealed, becomes a neutral card.
        // This is to prevent guessers to get the full board just by looking at the JSON response using Postman or something.
        final Team NEUTRAL_TEAM = new Team();
        for (int i = 0; i < wordCards.size(); i++) {
            final WordCardData originalCard = wordCards.get(i);
            if (!originalCard.isFound()) {
                wordCards.add(i, new WordCardData(new WordCard(originalCard.getWord(), NEUTRAL_TEAM, false)));
            }
        }
    }
}
