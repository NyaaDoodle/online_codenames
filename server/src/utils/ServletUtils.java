package utils;

import jakarta.servlet.ServletContext;
import lobby.LobbyManager;
import users.UserManager;
import utils.constants.Constants;

public class ServletUtils {
    private static final Object initLobbyManagerLock = new Object();
    private static final Object initUserManagerLock = new Object();

    public static LobbyManager getLobbyManager(ServletContext servletContext) {
        synchronized (initLobbyManagerLock) {
            if (servletContext.getAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME, new LobbyManager());
            }
            return (LobbyManager) servletContext.getAttribute(Constants.LOBBY_MANAGER_ATTRIBUTE_NAME);
        }
    }

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (initUserManagerLock) {
            if (servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
            return (UserManager) servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME);
        }
    }
}
