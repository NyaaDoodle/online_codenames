package game.room;

public class PlayerState {
    private final String game;
    private final String team;
    private final GameRole role;

    public PlayerState(final String game, final String team, final GameRole role) {
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
