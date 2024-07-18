package game.listing;

import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;

public class GameListing {
    private final GameStructure gameStructure;
    private ListingState state;
    private int connectedDefiners;
    private int connectedGuessers;

    public GameListing(GameStructure gameStructure) {
        this.gameStructure = gameStructure;
        this.state = ListingState.PENDING;
        this.connectedDefiners = 0;
        this.connectedGuessers = 0;
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public ListingState getState() {
        return state;
    }

    public int getConnectedDefiners() {
        return connectedDefiners;
    }

    public int getConnectedGuessers() {
        return connectedGuessers;
    }

    public void setState(ListingState state) {
        if (this.state.equals(ListingState.PENDING)) {
            this.state = state;
        }
    }

    public void incrementConnectedDefiners() {
        if (this.state.equals(ListingState.PENDING)) {
            connectedDefiners++;
        }
    }

    public void incrementConnectedGuessers() {
        if (this.state.equals(ListingState.PENDING)) {
            connectedGuessers++;
        }
    }
    public String getName() {
        return gameStructure.getName();
    }
    public int getRows() {
        return gameStructure.getBoard().getRows();
    }
    public int getColumns() {
        return gameStructure.getBoard().getColumns();
    }
    public String getDictionaryFileName() {
        return gameStructure.getDictionaryFileName();
    }
    public int getWordCount() {
        return gameStructure.getWords().size();
    }
    public int getRegularCardsCount() {
        return gameStructure.getBoard().getCardCount();
    }
    public int getBlackCardsCount() {
        return gameStructure.getBoard().getBlackCardCount();
    }
    public List<Team> getTeam() {
        return gameStructure.getTeams();
    }
}
