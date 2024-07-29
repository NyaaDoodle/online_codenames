package chat;

import org.jetbrains.annotations.NotNull;

public class TeamChatManager {
    private final ChatRoomManager allTeamChat = new ChatRoomManager();
    private final ChatRoomManager definersChat = new ChatRoomManager();

    @NotNull
    public ChatRoomManager getChatRoomManager(final ChatRoomType chatRoomType) {
        return (chatRoomType.equals(ChatRoomType.DEFINERS_CHAT)) ? definersChat : allTeamChat;
    }
}
