package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.LogUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;

@WebServlet(name = Constants.LOGOUT_SERVLET_NAME, urlPatterns = {Constants.LOGOUT_RESOURCE_URI})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) {
        final String sessionUsername = SessionUtils.getUsername(req);
        final UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (sessionUsername != null) {
            LogUtils.logToConsole("Removing user \"" + sessionUsername + "\" from users.");
            userManager.removeUser(sessionUsername);
            SessionUtils.clearSession(req);
            res.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
