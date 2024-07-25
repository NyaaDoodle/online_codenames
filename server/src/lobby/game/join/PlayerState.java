package lobby.game.join;

import game.structure.Team;
import jakarta.servlet.ServletContext;
import lobby.LobbyManager;
import lobby.game.list.GameListingData;
import utils.ServletUtils;

public class PlayerState {
    private GameListingData game;
    private Team team;
    private GameRole role;
    private String gameName;        // These two are here in case of an error,
    private String teamName;        // to report the original game and team names.

    public static PlayerState createFromPlayerStateIdentifiers(final PlayerStateIdentifiers playerStateIdentifiers, final LobbyManager lobbyManager) {
        final PlayerState playerState = new PlayerState();
        playerState.gameName = playerStateIdentifiers.getGame();
        playerState.teamName = playerStateIdentifiers.getTeam();
        playerState.game = lobbyManager.getGameListing(playerStateIdentifiers.getGame());
        if (playerState.game != null) {
            playerState.team = playerState.game.getTeam(playerStateIdentifiers.getTeam());
        }
        playerState.role = playerStateIdentifiers.getRole();
        return playerState;
    }

    public GameListingData getGame() {
        return game;
    }

    public Team getTeam() {
        return team;
    }

    public GameRole getRole() {
        return role;
    }

    public String getGameName() {
        return gameName;
    }

    public String getTeamName() {
        return teamName;
    }
}
