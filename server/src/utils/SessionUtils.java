package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lobby.game.join.PlayerState;
import org.jetbrains.annotations.Nullable;

public class SessionUtils {
    private static final String USERNAME_ATTRIBUTE_NAME = "username";

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
}
