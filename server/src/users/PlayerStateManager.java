package users;

import lobby.game.join.PlayerState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerStateManager {
    private final Map<String, PlayerState> playerStateMap = new HashMap<>();

    public PlayerStateManager(final UserManager userManager) {
        userManager.getUsers().forEach(user -> playerStateMap.put(user, null));
    }

    @Nullable
    public synchronized PlayerState getPlayerState(final String username) {
        return playerStateMap.get(username);
    }

    public synchronized void setPlayerState(final String username, final PlayerState playerState) {
        playerStateMap.put(username, playerState);
    }

    public synchronized void nullifyPlayerState(final String username) {
        playerStateMap.put(username, null);
    }

    public synchronized void nullifyPlayerStateByGame(final String gameName) {
        playerStateMap.keySet().forEach(user -> {
            final PlayerState playerState = playerStateMap.get(user);
            if (playerState != null && playerState.getGame().equals(gameName)) {
                nullifyPlayerState(user);
            }
        });
    }

    public synchronized void nullifyPlayerStateByTeam(final String teamName, final String gameName) {
        playerStateMap.keySet().forEach(user -> {
            final PlayerState playerState = playerStateMap.get(user);
            if (playerState != null && playerState.getGame().equals(gameName) && playerState.getTeam().equals(teamName)) {
                nullifyPlayerState(user);
            }
        });
    }
}
