package lobby.game.list;

import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;

public class GameListingData {
    private final GameStructure gameStructure;
    private final String name;
    private final ListingState state;
    private final ConnectedPlayersMap connectedPlayers;

    public GameListingData(GameListing gameListing) {
        this.gameStructure = gameListing.getGameStructure();
        this.name = gameListing.getGameStructure().getName();
        this.state = gameListing.getState();
        this.connectedPlayers = gameListing.getConnectedPlayers();
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

    public ConnectedPlayersMap getConnectedPlayers() {
        return connectedPlayers;
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

    public boolean isGamePending() { return state == ListingState.PENDING; }

    public boolean isGameActive() { return state == ListingState.ACTIVE; }

    public List<Team> getTeams() {
        return gameStructure.getTeams();
    }

    public int getConnectedDefinersByTeam(final String teamName) {
        return connectedPlayers.getConnectedDefinersByTeam(teamName);
    }

    public int getConnectedGuessersByTeam(final String teamName) {
        return connectedPlayers.getConnectedGuessersByTeam(teamName);
    }

    public int getTotalConnectedPlayers() {
        return connectedPlayers.getTotalConnectedPlayers();
    }

    public boolean areDefinersFull(final Team team) {
        return getConnectedDefinersByTeam(team.getName()) == team.getDefinersCount();
    }

    public boolean areGuessersFull(final Team team) {
        return getConnectedGuessersByTeam(team.getName()) == team.getGuessersCount();
    }

    public boolean isTeamFull(final Team team) {
        return areDefinersFull(team) && areGuessersFull(team);
    }

    public Team getTeam(final String teamName) {
        return gameStructure.getTeam(teamName);
    }
}
