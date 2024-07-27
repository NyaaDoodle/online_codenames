package game.instance;

import game.structure.Team;
import org.jetbrains.annotations.NotNull;
import utils.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class WinOrder {
    private final List<Pair<Team, EndPlayCause>> winOrder;
    private int currentNextWin;
    private int currentNextLose;

    public List<Pair<Team, EndPlayCause>> getWinOrder() {
        return winOrder;
    }

    public WinOrder(final int teamCount) {
        winOrder = new ArrayList<Pair<Team, EndPlayCause>>(teamCount);
        for (int i = 0; i < teamCount; i++) {
            winOrder.add(i, null);
        }
        currentNextWin = 0;
        currentNextLose = teamCount - 1;
    }

    public void addWinningTeam(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause) {
        if (currentNextWin <= currentNextLose) {
            winOrder.set(currentNextWin, new Pair<>(team, endPlayCause));
            currentNextWin++;
        }
    }

    public void addLosingTeam(@NotNull final Team team, @NotNull final EndPlayCause endPlayCause) {
        if (currentNextWin <= currentNextLose) {
            winOrder.set(currentNextLose, new Pair<>(team, endPlayCause));
            currentNextLose--;
        }
    }

    public int getWinNumberOfTeam(@NotNull final Team team) {
        for (int i = 0; i < winOrder.size(); i++) {
            final Pair<Team, EndPlayCause> entry = winOrder.get(i);
            if (entry != null && entry.getKey().equals(team)) { return i + 1; }
        }
        return Constants.ERROR_NUM;
    }

    public int getWinNumberOfTeam(@NotNull final String teamName) {
        for (int i = 0; i < winOrder.size(); i++) {
            final String checkTeamName = winOrder.get(i).getKey().getName();
            if (teamName.equals(checkTeamName)) {
                return i + 1;
            }
        }
        return Constants.ERROR_NUM;
    }
}
