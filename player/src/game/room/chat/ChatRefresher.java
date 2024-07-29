package game.room.chat;

import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import utils.constants.Constants;
import utils.http.HttpClientUtils;

import java.util.Objects;
import java.util.TimerTask;

public class ChatRefresher extends TimerTask {
    private static final String GET_CHAT_URL = Constants.BASE_URL + Constants.CHAT_RESOURCE_URI;
    public static final String CHAT_LAST_POSITION_PARAMETER = "lastpos";

    private final ChatRoomType chatRoomType;
    private final ChatRoom chatRoom;
    private int lastPos;

    public ChatRefresher(@NotNull final ChatRoomType chatRoomType, @NotNull final ChatRoom chatRoom) {
        this.chatRoomType = chatRoomType;
        this.chatRoom = chatRoom;
        lastPos = 0;
    }

    public synchronized void setLastPos(final int lastPos) {
        this.lastPos = lastPos;
    }

    @Override
    public void run() {
        final HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse(GET_CHAT_URL)).newBuilder()
                .addQueryParameter(Constants.CHAT_TYPE_PARAMETER, chatRoomType.toString())
                .addQueryParameter(CHAT_LAST_POSITION_PARAMETER, String.valueOf(lastPos))
                .build();
        HttpClientUtils.sendChatRoomGetRequestAsync(httpUrl, chatRoom);
    }
}
