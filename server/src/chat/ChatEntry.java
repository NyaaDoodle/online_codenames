package chat;

public class ChatEntry {
    private final String message;
    private final String username;

    public ChatEntry(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return ((username != null) ? username + ": " : "") + message;
    }
}
