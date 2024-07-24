package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lobby.LobbyManager;
import okhttp3.Request;
import users.UserManager;
import utils.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {
    private static final Object initLobbyManagerLock = new Object();
    private static final Object initUserManagerLock = new Object();

    public static LobbyManager getLobbyManager(final ServletContext servletContext) {
        synchronized (initLobbyManagerLock) {
            if (servletContext.getAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME, new LobbyManager());
            }
            return (LobbyManager) servletContext.getAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME);
        }
    }

    public static UserManager getUserManager(final ServletContext servletContext) {
        synchronized (initUserManagerLock) {
            if (servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
            return (UserManager) servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME);
        }
    }

    public static boolean isUserLoggedIn(final HttpServletRequest req, final ServletContext servletContext) {
        final String sessionUsername = SessionUtils.getUsername(req);
        final UserManager userManager = getUserManager(servletContext);
        return (sessionUsername != null) && (!sessionUsername.isEmpty()) && (userManager.doesUserExist(sessionUsername));
    }

    public static String getRequestBody(final HttpServletRequest req) throws IOException {
        try (final BufferedReader reader = new BufferedReader(req.getReader())) {
            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(stringBuilder::append);
            return stringBuilder.toString();
        }
    }
}
