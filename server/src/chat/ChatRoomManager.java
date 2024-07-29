package chat;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomManager {
    private final List<ChatEntry> chatEntriesList = new ArrayList<>();

    public synchronized void addChatEntry(final String message, final String username) {
        chatEntriesList.add(new ChatEntry(message, username));
    }

    public synchronized List<ChatEntry> getChatEntries(int startingPosition) {
        final int lastPos = getLastPosition();
        if (startingPosition < 0 || startingPosition > lastPos) {
            startingPosition = 0;
        }
        return chatEntriesList.subList(startingPosition, lastPos);
    }

    public int getLastPosition() {
        return chatEntriesList.size();
    }
}
