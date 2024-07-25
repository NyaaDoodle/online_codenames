package lobby.game.join;

public class PlayerStateIdentifiers {
    private final String game;
    private final String team;
    private final GameRole role;

    public PlayerStateIdentifiers(String game, String team, GameRole role) {
        this.game = game;
        this.team = team;
        this.role = role;
    }

    public String getGame() {
        return game;
    }

    public String getTeam() {
        return team;
    }

    public GameRole getRole() {
        return role;
    }
}
