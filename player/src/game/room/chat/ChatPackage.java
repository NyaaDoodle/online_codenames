package game.room.chat;

import java.util.List;

public class ChatPackage {
    private final List<ChatEntry> chatEntries;
    private final int lastPosition;

    public ChatPackage(List<ChatEntry> chatEntries, int lastPosition) {
        this.chatEntries = chatEntries;
        this.lastPosition = lastPosition;
    }

    public List<ChatEntry> getChatEntries() {
        return chatEntries;
    }

    public int getLastPosition() {
        return lastPosition;
    }
}
