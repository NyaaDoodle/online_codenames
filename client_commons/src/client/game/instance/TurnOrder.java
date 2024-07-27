package client.game.instance;

import client.game.structure.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TurnOrder {
    private final LinkedList<Team> queue;
    private final Team currentTurn;
    private final GameRole currentRole;
    private final Map<String, Integer> teamNameToTurnCount;

    public TurnOrder(LinkedList<Team> queue, Team currentTurn, GameRole currentRole, Map<String, Integer> teamNameToTurnCount) {
        this.queue = queue;
        this.currentTurn = currentTurn;
        this.currentRole = currentRole;
        this.teamNameToTurnCount = teamNameToTurnCount;
    }

    public Queue<Team> getQueue() {
        return new LinkedList<>(queue);
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public GameRole getCurrentRole() {
        return currentRole;
    }

    public Team getNextTurn() {
        return queue.element();
    }

    public Map<String, Integer> getTeamNameToTurnCount() {
        return Collections.unmodifiableMap(teamNameToTurnCount);
    }

    public int getTurnCountByTeam(final String teamName) {
        return teamNameToTurnCount.get(teamName);
    }

    public int getTotalTurns() {
        return teamNameToTurnCount.values().stream().mapToInt(i -> i).sum();
    }

    public int numberOfTeamsInPlay() {
        return queue.size();
    }

    public boolean isOneTeamLeft() {
        return numberOfTeamsInPlay() == 1;
    }

    public boolean isTeamInPlay(final Team team) {
        return queue.contains(team);
    }
}
