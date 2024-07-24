package lobby.game.list;

import game.structure.GameStructure;
import game.structure.Team;

import java.util.List;

public class GameListingData {
    private GameStructure gameStructure;
    private String name;
    private ListingState state;
    private ConnectedPlayersMap connectedPlayers;

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

    public void setGameStructure(GameStructure gameStructure) {
        this.gameStructure = gameStructure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(ListingState state) {
        this.state = state;
    }

    public void setConnectedPlayers(ConnectedPlayersMap connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
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
