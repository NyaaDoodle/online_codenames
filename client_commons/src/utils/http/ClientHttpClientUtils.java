package utils.http;

import login.LoginController;
import login.exceptions.UsernameInputException;
import okhttp3.OkHttpClient;

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

    public static void AttemptToLogin(final String username) throws UsernameInputException, IOException {
        LoginController.attemptToConnect(HTTP_CLIENT, username);
    }

    public static void LogoutFromServer() throws IOException {
        LoginController.logout(HTTP_CLIENT);
    }

    public static void closeHttpClient() {
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
