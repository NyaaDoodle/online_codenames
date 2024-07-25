package game.join;

import game.structure.Team;
import lobby.game.list.GameListingData;

public class TempPlayerState {
    private GameListingData selectedGame = null;
    private Team selectedTeam = null;
    private GameRole selectedRole = null;

    public GameListingData getSelectedGame() {
        return selectedGame;
    }

    public void setSelectedGame(GameListingData selectedGame) {
        this.selectedGame = selectedGame;
    }

    public Team getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public GameRole getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(GameRole selectedRole) {
        this.selectedRole = selectedRole;
    }

    public String getGameName() {
        return selectedGame.getName();
    }

    public String getTeamName() {
        return selectedTeam.getName();
    }
}
