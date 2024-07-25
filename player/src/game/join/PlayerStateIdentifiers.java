package game.join;

public class PlayerStateIdentifiers {
    private final String game;
    private final String team;
    private final GameRole role;

    public PlayerStateIdentifiers(final String game, final String team, final GameRole role) {
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
