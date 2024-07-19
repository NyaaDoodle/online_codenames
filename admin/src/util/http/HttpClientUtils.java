package util.http;

import login.LoginController;
import login.exceptions.UsernameInputException;
import okhttp3.OkHttpClient;

import java.io.IOException;

public class HttpClientUtils {
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void AttemptToLogin(final String username) throws UsernameInputException, IOException {
        LoginController.AttemptToConnect(HTTP_CLIENT, username);
    }
    public static void LogoutFromServer() throws IOException {
        LoginController.Logout(HTTP_CLIENT);
    }
    public static void closeHttpClient() {
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
