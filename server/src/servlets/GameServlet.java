package servlets;

import game.data.GameData;
import game.instance.GameInstanceData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.game.join.PlayerState;
import lobby.game.list.GameListingData;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;

@WebServlet(name = Constants.GAME_SERVLET_NAME, urlPatterns = {Constants.GAME_RESOURCE_URI})
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final PlayerState playerState = SessionUtils.getPlayerState(req);
            if (playerState != null && playerState.getGame() != null) {
                final GameListingData gameListingData = ServletUtils.getLobbyManager(getServletContext()).getGameListing(playerState.getGame());
                if (gameListingData != null) {
                    // TODO GameInstance
                    final GameInstanceData gameInstanceData = (gameListingData.isGameActive()) ? null : null;
                    final GameData gameData = new GameData(gameListingData, gameInstanceData);
                    final String jsonBody = JSONUtils.toJson(gameData);
                    ResponseUtils.sendJSONSuccess(res, jsonBody);
                }
                else {
                    throw new ServletException("gameListingData fetching resulted in null");
                }

            }
            else {
                // TODO send 500 message informing the issue
                throw new ServletException("playerState or gameName null at fetching gameListingData");
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }
}
