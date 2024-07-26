package game.instance;

import game.structure.Team;
import lobby.game.join.GameRole;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TurnOrder {
    private final LinkedList<Team> queue;
    private Team currentTurn = null;
    private GameRole currentRole = null;
    private final Map<Team, Integer> teamToTurnCount = new HashMap<>();

    public TurnOrder(@NotNull final LinkedList<Team> turnOrder) {
        this.queue = turnOrder;
        turnOrder.forEach(team -> teamToTurnCount.put(team, 0));
    }

    public void moveToNextTurn() {
        if (currentRole.equals(GameRole.DEFINER)) {
            currentRole = GameRole.GUESSER;
        }
        else {
            currentTurn = queue.remove();
            currentRole = GameRole.DEFINER;
            queue.add(currentTurn);
            teamToTurnCount.put(currentTurn, teamToTurnCount.get(currentTurn) + 1);
        }
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

    public Map<Team, Integer> getTeamToTurnCount() {
        return Collections.unmodifiableMap(teamToTurnCount);
    }

    public int getTotalTurns() {
        return teamToTurnCount.values().stream().mapToInt(i -> i).sum();
    }

    public void removeTeam(final Team team) {
        queue.remove(team);
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
