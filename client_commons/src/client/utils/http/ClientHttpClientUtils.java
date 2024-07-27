package client.utils.http;

import client.exceptions.NoPlayerStatusAtServerException;
import client.exceptions.JoinGameException;
import client.utils.constants.ClientConstants;
import client.login.ClientLoginController;
import client.login.exceptions.UsernameInputException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ClientHttpClientUtils {
    private static final SimpleCookieManager COOKIE_MANAGER = new SimpleCookieManager();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(COOKIE_MANAGER).build();

    private static final String JOIN_GAME_ERROR_HEADER = "Error-Type";
    private static final String NO_PLAYER_STATUS_HEADER = "No-Player-Status";

    protected static SimpleCookieManager getCookieManager() { return COOKIE_MANAGER; }

    protected static OkHttpClient getHttpClient() { return HTTP_CLIENT; }

    public static void removeCookiesOf(String domain) {
        COOKIE_MANAGER.removeCookiesOf(domain);
    }

    @NotNull
    public static String sendRequestSync(final Request req) throws Exception {
        final Call call = HTTP_CLIENT.newCall(req);
        try (final Response res = call.execute()) {
            final String responseBody = (res.body() != null) ? res.body().string() : "";
            if (res.code() != ClientConstants.STATUS_CODE_OK) {
                throw new Exception(responseBody);
            }
            return responseBody;
        }
    }

    public static void attemptToLogin(final String username) throws UsernameInputException, IOException {
        ClientLoginController.attemptToConnect(HTTP_CLIENT, username);
    }

    public static void logoutFromServer() throws IOException {
        ClientLoginController.attemptToLogout(HTTP_CLIENT);
    }

    public static void closeHttpClient() {
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static void sendJoinGameRequest(final Request req) throws Exception {
        final Call call = HTTP_CLIENT.newCall(req);
        try (final Response res = call.execute()) {
            final String responseBody = (res.body() != null) ? res.body().string() : "";
            if (res.code() != ClientConstants.STATUS_CODE_OK) {
                final String errorType = res.header(JOIN_GAME_ERROR_HEADER);
                throw new JoinGameException(errorType, responseBody);
            }
        }
    }

    public static String sendGameRequest(final Request req) throws Exception {
        final Call call = HTTP_CLIENT.newCall(req);
        try (final Response res = call.execute()) {
            final String responseBody = (res.body() != null) ? res.body().string() : "";
            if (res.code() != ClientConstants.STATUS_CODE_OK) {
                if (res.code() == ClientConstants.STATUS_CODE_CONFLICT) {
                    final String noPlayerStatus = res.header(NO_PLAYER_STATUS_HEADER);
                    if (noPlayerStatus != null && noPlayerStatus.equals(String.valueOf(true))) {
                        throw new NoPlayerStatusAtServerException("The game has ended. Exiting game room.");
                    }
                    else {
                        throw new Exception(responseBody);
                    }
                }
                else {
                    throw new Exception(responseBody);
                }
            }
            return responseBody;
        }
    }
}
