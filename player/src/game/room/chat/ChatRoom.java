package game.room.chat;

import client.game.instance.GameRole;
import com.google.gson.JsonSyntaxException;
import input.InputController;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.constants.Constants;
import utils.http.HttpClientUtils;
import utils.json.JSONUtils;

import java.util.Objects;
import java.util.Timer;

public class ChatRoom {
    private static final String SEND_CHAT_URL = Constants.BASE_URL + Constants.SEND_CHAT_RESOURCE_URI;
    private static final String CHAT_MESSAGE_PARAMETER = "message";
    private static final int REFRESH_RATE = 2000;

    private final ChatRoomType chatRoomType;
    private final GameRole gameRole;

    private ChatRefresher chatRefresher;
    private Timer timer;

    private boolean quitChat = false;

    public ChatRoom(@NotNull final ChatRoomType chatRoomType, @NotNull final GameRole gameRole) {
        this.chatRoomType = chatRoomType;
        this.gameRole = gameRole;
    }

    public void goToChatRoom() {
        greeter();
        initiateChatRefresher();
        while (!quitChat) {
            writeMessage();
        }
        shutdownChatRefresher();
        leaveMessage();
    }

    public void setQuitChat() {
        quitChat = true;
    }

    public void parseChatPackage(final String json) {
        try {
            final ChatPackage chatPackage = JSONUtils.fromJson(json, ChatPackage.class);
            chatRefresher.setLastPos(chatPackage.getLastPosition());
            chatPackage.getChatEntries().forEach(System.out::println);
        } catch (JsonSyntaxException e) {
            UIElements.unexpectedExceptionMessage(e);
        }
    }

    private void greeter() {
        System.out.println("You have joined the " + (chatRoomType.equals(ChatRoomType.DEFINERS_CHAT) ? "definers-only chat" : "all-team chat") + "!");
    }

    private void leaveMessage() {
        System.out.println("You have left the chatroom.");
    }

    private void writeMessage() {
        final String message = InputController.chatMessageInput();
        if (message.equals(Constants.QUIT_STRING)) {
            quitChat = true;
            return;
        }
        if (gameRole.equals(GameRole.DEFINER) && chatRoomType.equals(ChatRoomType.ALL_TEAM_CHAT)) {
            System.out.println("Definers are not allowed to send messages on the team chat, only in definers-only chat.");
            return;
        }
        final HttpUrl finalUrl = Objects.requireNonNull(HttpUrl.parse(SEND_CHAT_URL)).newBuilder()
                .addQueryParameter(Constants.CHAT_TYPE_PARAMETER, chatRoomType.toString())
                .addQueryParameter(CHAT_MESSAGE_PARAMETER, message)
                .build();
        HttpClientUtils.sendChatRoomSendRequestAsync(finalUrl, this);
    }

    private void initiateChatRefresher() {
        chatRefresher = new ChatRefresher(chatRoomType, this);
        timer = new Timer();
        timer.schedule(chatRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void shutdownChatRefresher() {
        chatRefresher.cancel();
        timer.cancel();
    }
}
