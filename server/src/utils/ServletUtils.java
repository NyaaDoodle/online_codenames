package utils;

import chat.ChatManager;
import game.engine.GameEngine;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lobby.LobbyManager;
import org.jetbrains.annotations.NotNull;
import users.PlayerStateManager;
import users.UserManager;
import utils.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {
    private static final String LOBBY_MANAGER_ATTRIBUTE_NAME = "lobbyManager";
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String GAME_ENGINE_ATTRIBUTE_NAME = "gameEngine";
    private static final String PLAYER_STATE_MANAGER_ATTRIBUTE_NAME = "playerStateManager";

    private static final Object initLobbyManagerLock = new Object();
    private static final Object initUserManagerLock = new Object();
    private static final Object initChatManagerLock = new Object();
    private static final Object initGameEngineLock = new Object();
    private static final Object initPlayerStateManagerLock = new Object();

    @NotNull
    public static LobbyManager getLobbyManager(final ServletContext servletContext) {
        synchronized (initLobbyManagerLock) {
            if (servletContext.getAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME, new LobbyManager());
            }
        }
        return (LobbyManager) servletContext.getAttribute(LOBBY_MANAGER_ATTRIBUTE_NAME);
    }

    @NotNull
    public static UserManager getUserManager(final ServletContext servletContext) {
        synchronized (initUserManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    @NotNull
    public static GameEngine getGameEngine(final ServletContext servletContext) {
        synchronized (initGameEngineLock) {
            if (servletContext.getAttribute(GAME_ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(GAME_ENGINE_ATTRIBUTE_NAME, new GameEngine());
            }
        }
        return (GameEngine) servletContext.getAttribute(GAME_ENGINE_ATTRIBUTE_NAME);
    }

    @NotNull
    public static ChatManager getChatManager(final ServletContext servletContext) {
        synchronized (initChatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    @NotNull
    public static PlayerStateManager getPlayerStateManager(final ServletContext servletContext) {
        synchronized (initPlayerStateManagerLock) {
            if (servletContext.getAttribute(PLAYER_STATE_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(PLAYER_STATE_MANAGER_ATTRIBUTE_NAME, new PlayerStateManager(getUserManager(servletContext)));
            }
        }
        return (PlayerStateManager) servletContext.getAttribute(PLAYER_STATE_MANAGER_ATTRIBUTE_NAME);
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

    public static boolean isAdmin(final HttpServletRequest req) {
        final String sessionUsername = SessionUtils.getUsername(req);
        return Constants.ADMIN_USERNAMES.contains(sessionUsername);
    }
}
