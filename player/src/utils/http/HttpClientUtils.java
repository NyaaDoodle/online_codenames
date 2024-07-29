package utils.http;

import client.utils.http.ClientHttpClientUtils;
import game.room.chat.ChatRoom;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.constants.Constants;

import java.io.IOException;

public class HttpClientUtils extends ClientHttpClientUtils {
    private static final OkHttpClient HTTP_CLIENT = getHttpClient();

    public static void sendChatRoomSendRequestAsync(final HttpUrl httpUrl, final ChatRoom chatRoom) {
        Request request = new Request.Builder().get().url(httpUrl).build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Failed to send message to the server, try again...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handleUnsuccessfulResponse(response, chatRoom);
                }
            }
        });
    }

    public static void sendChatRoomGetRequestAsync(final HttpUrl httpUrl, final ChatRoom chatRoom) {
        Request request = new Request.Builder().get().url(httpUrl).build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Failed to update chat entries...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handleUnsuccessfulResponse(response, chatRoom);
                }
                else {
                    final String responseBody = (response.body() != null) ? response.body().string() : "";
                    chatRoom.parseChatPackage(responseBody);
                }
            }
        });
    }

    private static void handleUnsuccessfulResponse(@NotNull Response res, final ChatRoom chatRoom) throws IOException {
        final String responseBody = (res.body() != null) ? res.body().string() : "";
        if (res.code() == Constants.STATUS_CODE_CONFLICT) {
            final String noPlayerStatus = res.header(NO_PLAYER_STATUS_HEADER);
            if (noPlayerStatus != null && noPlayerStatus.equals(String.valueOf(true))) {
                chatRoom.setQuitChat();
            }
            else {
                System.out.println(responseBody);
            }
        }
        else {
            System.out.println(responseBody);
        }
    }
}
