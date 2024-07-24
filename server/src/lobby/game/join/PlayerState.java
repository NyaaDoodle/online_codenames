package lobby.game.join;

public class PlayerState {
    private final String game;
    private final String team;
    private final String role;

    public PlayerState(String game, String team, String role) {
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

    public String getRole() {
        return role;
    }
}
