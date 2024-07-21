package utils.http;

import login.LoginController;
import login.exceptions.UsernameInputException;
import okhttp3.OkHttpClient;

import java.io.IOException;

public class HttpClientUtils {
    private final static SimpleCookieManager cookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(cookieManager).build();

    public static void removeCookiesOf(String domain) {
        cookieManager.removeCookiesOf(domain);
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
