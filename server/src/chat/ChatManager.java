package chat;

import game.structure.Team;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager {
    private final Map<String, GameChatManager> allChats = new HashMap<>();

    @Nullable
    public GameChatManager getGameChatManager(final String gameName) {
        return allChats.get(gameName);
    }

    @Nullable
    public TeamChatManager getTeamChatManager(final String gameName, final String teamName) {
        final GameChatManager gameChatManager = getGameChatManager(gameName);
        return (gameChatManager != null) ? gameChatManager.getTeamChatManager(teamName) : null;
    }

    @Nullable
    public ChatRoomManager getChatRoomManager(final String gameName, final String teamName, final ChatRoomType chatRoomType) {
        final TeamChatManager teamChatManager = getTeamChatManager(gameName, teamName);
        return (teamChatManager != null) ? teamChatManager.getChatRoomManager(chatRoomType) : null;
    }

    public synchronized void addChat(final String gameName, final List<Team> teams) {
        allChats.put(gameName, new GameChatManager(teams));
    }

    public synchronized void blankGameChats(final String gameName) {
        final GameChatManager gameChatManager = getGameChatManager(gameName);
        if (gameChatManager != null) {
            gameChatManager.blankAllChats();
        }
    }

    public synchronized void blankTeamChat(final String gameName, final String teamName) {
        final GameChatManager gameChatManager = getGameChatManager(gameName);
        if (gameChatManager != null) {
            gameChatManager.blankTeamChat(teamName);
        }
    }
}
