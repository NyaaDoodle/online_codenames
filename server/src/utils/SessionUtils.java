package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import utils.constants.Constants;

public class SessionUtils {
    public static String getUsername (final HttpServletRequest req) {
        HttpSession httpSession = req.getSession(false);
        Object sessionAttribute = (httpSession != null) ? httpSession.getAttribute(Constants.USERNAME_ATTRIBUTE_NAME) : null;
        return (sessionAttribute != null) ? sessionAttribute.toString() : null;
    }
    public static void clearSession (final HttpServletRequest req) {
        req.getSession().invalidate();
    }
}
