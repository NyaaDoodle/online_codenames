package client.login;

import client.login.exceptions.UsernameInputException;
import client.ui.ClientUIElements;
import client.utils.constants.ClientConstants;
import client.utils.http.ClientHttpClientUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

public class ClientLoginController {
    // For now, only admin is reserved.
    private static final Set<String> reservedUsernames = new HashSet<>(Collections.singletonList("admin"));
    private static final String LOGIN_URL = ClientConstants.BASE_URL + ClientConstants.LOGIN_RESOURCE_URI;
    private static final String LOGOUT_URL = ClientConstants.BASE_URL + ClientConstants.LOGOUT_RESOURCE_URI;
    private static final String USERNAME_QUERY_NAME = "username";

    public static void validateUsernameInput(final String username) throws UsernameInputException {
        if (username == null || username.isEmpty()) {
            throw new UsernameInputException("A blank username cannot be used.");
        }
        else if (reservedUsernames.contains(username)) {
            throw new UsernameInputException("The username \"" + username + "\" is reserved. Please use a different username.");
        }
    }

    public static void attemptToConnect(final OkHttpClient httpClient, final String username) throws UsernameInputException, IOException {
        final HttpUrl finalURL = Objects.requireNonNull(HttpUrl.parse(LOGIN_URL)).newBuilder().addQueryParameter(USERNAME_QUERY_NAME, username).build();
        final Request req = new Request.Builder().get().url(finalURL).build();
        final Call call = httpClient.newCall(req);
        try (Response res = call.execute()) {
            if (res.code() != ClientConstants.STATUS_CODE_OK) {
                assert res.body() != null;
                throw new UsernameInputException(res.body().string());
            }
        }
    }

    public static void logout() {
        boolean logoutSuccess = false;
        while (!logoutSuccess) {
            try {
                ClientUIElements.logoutMessage();
                ClientHttpClientUtils.logoutFromServer();
                logoutSuccess = true;
            }
            catch (IOException e) {
                ClientUIElements.unexpectedIOExceptionMessage(e);
            }
        }
    }

    public static void attemptToLogout(final OkHttpClient httpClient) throws IOException {
        final Request req = new Request.Builder().get().url(LOGOUT_URL).build();
        final Call call = httpClient.newCall(req);
        try (Response res = call.execute()) {
            if (res.code() == ClientConstants.STATUS_CODE_OK) {
                ClientHttpClientUtils.removeCookiesOf(ClientConstants.SERVER_NAME);
            }
        }
    }
}
