package game.instance;

import game.structure.Team;

public class TeamLeftEntry {
    private final Team team;
    private final EndPlayCause endPlayCause;
    private final int winPosition;

    public TeamLeftEntry(Team team, EndPlayCause endPlayCause, int winPosition) {
        this.team = team;
        this.endPlayCause = endPlayCause;
        this.winPosition = winPosition;
    }

    public Team getTeam() {
        return team;
    }

    public EndPlayCause getEndPlayCause() {
        return endPlayCause;
    }

    public int getWinPosition() {
        return winPosition;
    }
}
