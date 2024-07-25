package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lobby.game.join.PlayerState;
import lobby.game.join.PlayerStateIdentifiers;
import org.jetbrains.annotations.Nullable;

public class SessionUtils {
    private static final String USERNAME_ATTRIBUTE_NAME = "username";
    private static final String PLAYER_STATE_ATTRIBUTE_NAME = "playerStatus";

    public static String getUsername(final HttpServletRequest req) {
        final HttpSession httpSession = req.getSession(false);
        final Object sessionUsername = (httpSession != null) ? httpSession.getAttribute(USERNAME_ATTRIBUTE_NAME) : null;
        return (sessionUsername != null) ? sessionUsername.toString() : null;
    }

    public static void setUsername(final HttpServletRequest req, final String username) {
        req.getSession(true).setAttribute(USERNAME_ATTRIBUTE_NAME, username);
    }

    public static void clearSession(final HttpServletRequest req) {
        req.getSession().invalidate();
    }

    public static PlayerState getPlayerState(final HttpServletRequest req) {
        final HttpSession httpSession = req.getSession(false);
        final Object sessionPlayerStatus = (httpSession != null) ? httpSession.getAttribute(PLAYER_STATE_ATTRIBUTE_NAME) : null;
        return (sessionPlayerStatus != null) ? (PlayerState)sessionPlayerStatus : null;
    }

    public static void setPlayerState(final HttpServletRequest req, @Nullable final PlayerState playerState) {
        final HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            httpSession.setAttribute(PLAYER_STATE_ATTRIBUTE_NAME, playerState);
        }
    }

    public static void nullifyPlayerStatus(final HttpServletRequest req) {
        setPlayerState(req, null);
    }

    public static boolean isUserInAGame(final HttpServletRequest req) {
        return getPlayerState(req) != null;
    }
}
