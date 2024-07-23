package utils.http;

import login.ClientLoginController;
import login.exceptions.UsernameInputException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.constants.ClientConstants;

import java.io.IOException;

public class ClientHttpClientUtils {
    private static final SimpleCookieManager COOKIE_MANAGER = new SimpleCookieManager();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(COOKIE_MANAGER).build();

    protected static SimpleCookieManager getCookieManager() { return COOKIE_MANAGER; }

    protected static OkHttpClient getHttpClient() { return HTTP_CLIENT; }

    public static void removeCookiesOf(String domain) {
        COOKIE_MANAGER.removeCookiesOf(domain);
    }

    @NotNull
    public static String sendRequestSync(final Request req) throws Exception {
        final Call call = HTTP_CLIENT.newCall(req);
        try (Response res = call.execute()) {
            if (res.code() != ClientConstants.STATUS_CODE_OK) {
                assert res.body() != null;
                throw new Exception(res.body().string());
            }
            return (res.body() != null) ? res.body().string() : "";
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
}
