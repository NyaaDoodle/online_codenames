package servlets;

import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.game.join.PlayerState;
import users.UserManager;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@WebServlet(name = Constants.JOIN_GAME_SERVLET_NAME, urlPatterns = {Constants.JOIN_GAME_RESOURCE_URI})
public class JoinGameServlet extends HttpServlet {
    private static final String GAME_FULL_ERROR = "GAME_FULL";
    private static final String TEAM_FULL_ERROR = "TEAM_FULL";
    private static final String ROLE_FULL_ERROR = "ROLE_FULL";
    private static final String BAD_JSON_ERROR = "BAD_SYNTAX";
    private static final String ALREADY_JOINED_GAME_ERROR = "ALREADY_JOINED";

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            if (!SessionUtils.isUserInAGame(req)) {
                final String jsonBody = ServletUtils.getRequestBody(req);
                try {
                    final PlayerState playerState = JSONUtils.fromJson(jsonBody, PlayerState.class);

                } catch (JsonSyntaxException e) {
                    ResponseUtils.sendJoinGameError(res, BAD_JSON_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else {
                ResponseUtils.sendJoinGameError(res, ALREADY_JOINED_GAME_ERROR, HttpServletResponse.SC_CONFLICT);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }

    private void checkIfGameIsFull(final PlayerState playerState) {
        
    }
}
