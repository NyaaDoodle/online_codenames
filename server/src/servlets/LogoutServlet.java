package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        final String sessionUsername = SessionUtils.getUsername(req);
        final UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (sessionUsername != null) {
            userManager.removeUser(sessionUsername);
            SessionUtils.clearSession(req);
            res.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
