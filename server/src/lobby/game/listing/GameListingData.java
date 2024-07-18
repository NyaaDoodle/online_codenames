package lobby.game.listing;

import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;

public class GameListingData {
    private final GameStructure gameStructure;
    private final String name;
    private final ListingState state;
    private final int connectedDefiners;
    private final int connectedGuessers;

    public GameListingData(GameListing gameListing) {
        this.gameStructure = gameListing.getGameStructure();
        this.name = gameListing.getGameStructure().getName();
        this.state = gameListing.getState();
        this.connectedDefiners = gameListing.getConnectedDefiners();
        this.connectedGuessers = gameListing.getConnectedGuessers();
    }

    public GameStructure getGameStructure() {
        return gameStructure;
    }

    public String getName() {
        return name;
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
