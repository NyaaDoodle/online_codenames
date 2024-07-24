package utils.http;

import exceptions.JoinGameException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.constants.Constants;

public class HttpClientUtils extends ClientHttpClientUtils {
    private static final OkHttpClient HTTP_CLIENT = getHttpClient();
    private static final String JOIN_GAME_ERROR_HEADER = "Error-Type";

    public static void sendJoinGameRequest(final Request req) throws Exception {
        final Call call = HTTP_CLIENT.newCall(req);
        try (final Response res = call.execute()) {
            final String responseBody = (res.body() != null) ? res.body().string() : "";
            if (res.code() != Constants.STATUS_CODE_OK) {
                final String errorType = res.header(JOIN_GAME_ERROR_HEADER);
                throw new JoinGameException(errorType, responseBody);
            }
        }
    }
}
