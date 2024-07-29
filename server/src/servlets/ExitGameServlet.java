package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.PlayerStateManager;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;

import java.io.IOException;

@WebServlet(name = Constants.EXIT_GAME_SERVLET_NAME, urlPatterns = {Constants.EXIT_GAME_RESOURCE_URI})
public class ExitGameServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final String username = SessionUtils.getUsername(req);
            assert username != null;
            if (Constants.ADMIN_USERNAMES.contains(username)) {
                synchronized (this) {
                    final PlayerStateManager playerStateManager = ServletUtils.getPlayerStateManager(getServletContext());
                    playerStateManager.nullifyPlayerState(username);
                }
            }
            else {
                ResponseUtils.sendUnauthorized(res);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }
}
