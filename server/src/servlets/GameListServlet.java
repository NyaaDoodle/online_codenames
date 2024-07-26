package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.LobbyManager;
import lobby.game.list.GameList;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.constants.Constants;

import java.io.IOException;

@WebServlet(name = Constants.GAME_LIST_SERVLET_NAME, urlPatterns = {Constants.GAME_LIST_RESOURCE_URI})
public class GameListServlet extends HttpServlet {

    // only_actives takes priority over with_actives.
    private static final String ACTIVE_ONLY_PARAMETER_NAME = "only-actives";
    private static final String ACTIVE_INCLUDE_PARAMETER_NAME = "with-actives";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        // Non-logged-in users could also send requests, if they really want to.
        final boolean onlyActive = Boolean.parseBoolean(req.getParameter(ACTIVE_ONLY_PARAMETER_NAME));
        final boolean includeActive = Boolean.parseBoolean(req.getParameter(ACTIVE_INCLUDE_PARAMETER_NAME));
        final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
        final GameList gameList = lobbyManager.getGameList(onlyActive, includeActive);
        final Gson gson = new Gson();
        final String jsonBody = gson.toJson(gameList);
        ResponseUtils.sendJSONSuccess(res, jsonBody);
    }
}
