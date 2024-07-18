package utils;

import jakarta.servlet.ServletContext;
import lobby.LobbyManager;

public class ServletUtils {
    private static final String LOBBY_MANAGER_ATTRIBUTE_NAME = "lobbyManager";
    private static final Object initLobbyManagerLock = new Object();

    public static LobbyManager getLobbyManager(ServletContext servletContext) {
        synchronized (initLobbyManagerLock) {
            if (servletContext.getAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME, new LobbyManager());
            }
            return (LobbyManager) servletContext.getAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME);
        }
    }
}
