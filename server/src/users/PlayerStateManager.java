package users;

import lobby.game.join.PlayerState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerStateManager {
    private final Map<String, PlayerState> playerStateMap = new HashMap<>();

    @Nullable
    public synchronized PlayerState getPlayerState(final String username) {
        return playerStateMap.get(username);
    }

    public synchronized void setPlayerState(final String username, final PlayerState playerState) {
        playerStateMap.put(username, playerState);
    }

    public synchronized void nullifyPlayerState(final String username) {
        playerStateMap.remove(username);
    }

    public synchronized void nullifyPlayerStateByGame(final String gameName) {
        final Set<String> keys = new HashSet<>(playerStateMap.keySet());
        keys.forEach(user -> {
            final PlayerState playerState = playerStateMap.get(user);
            if (playerState != null && playerState.getGame().equals(gameName)) {
                nullifyPlayerState(user);
            }
        });
    }

    public synchronized void nullifyPlayerStateByTeam(final String teamName, final String gameName) {
        final Set<String> keys = new HashSet<>(playerStateMap.keySet());
        keys.forEach(user -> {
            final PlayerState playerState = playerStateMap.get(user);
            if (playerState != null && playerState.getGame().equals(gameName) && playerState.getTeam().equals(teamName)) {
                nullifyPlayerState(user);
            }
        });
    }
}
