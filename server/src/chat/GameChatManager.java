package chat;

import game.structure.Team;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameChatManager {
    private final Map<String, TeamChatManager> gameChats = new HashMap<>();

    public GameChatManager(final List<Team> teams) {
        teams.forEach(team -> gameChats.put(team.getName(), new TeamChatManager()));
    }

    @Nullable
    public TeamChatManager getTeamChatManager(final String teamName) {
        return gameChats.get(teamName);
    }

    @Nullable
    public ChatRoomManager getChatRoomManager(final String teamName, final ChatRoomType chatRoomType) {
        final TeamChatManager teamChatManager = getTeamChatManager(teamName);
        return (teamChatManager != null) ? teamChatManager.getChatRoomManager(chatRoomType) : null;
    }

    public synchronized void blankAllChats() {
        Set<String> keys = new HashSet<>(gameChats.keySet());
        keys.forEach(team -> gameChats.put(team, new TeamChatManager()));
    }

    public synchronized void blankTeamChat(final String teamName) {
        gameChats.put(teamName, new TeamChatManager());
    }
}
