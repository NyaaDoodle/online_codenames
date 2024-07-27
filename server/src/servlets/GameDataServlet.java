package servlets;

import game.data.GameData;
import game.engine.GameEngine;
import game.instance.data.GameInstanceData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import lobby.game.list.GameListingData;
import okhttp3.internal.ws.RealWebSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import users.PlayerStateManager;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;

@WebServlet(name = Constants.GAME_DATA_SERVLET_NAME, urlPatterns = {Constants.GAME_DATA_RESOURCE_URI})
public class GameDataServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final String username = SessionUtils.getUsername(req);
            final PlayerStateManager playerStateManager = ServletUtils.getPlayerStateManager(getServletContext());
            final PlayerState playerState = playerStateManager.getPlayerState(username);
            if (playerState != null) {
                final GameListingData gameListingData = ServletUtils.getLobbyManager(getServletContext()).getGameListing(playerState.getGame());
                if (gameListingData != null) {
                    final GameInstanceData gameInstanceData = getGameInstanceData(gameListingData, playerState.getRole());
                    final GameData gameData = new GameData(gameListingData, gameInstanceData);
                    final String jsonBody = JSONUtils.toJson(gameData);
                    ResponseUtils.sendJSONSuccess(res, jsonBody);
                }
                else {
                    throw new ServletException("gameListingData was null when retrieving from valid PlayerState");
                }
            }
            else {
                ResponseUtils.sendNoPlayerStatusError(res);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }

    @Nullable
    private GameInstanceData getGameInstanceData(@NotNull final GameListingData gameListingData, @NotNull final GameRole role) {
        final GameEngine gameEngine = ServletUtils.getGameEngine(getServletContext());
        if (gameListingData.isGameActive()) {
            switch (role) {
                case DEFINER:
                    return gameEngine.getGameInstanceDataFull(gameListingData.getName());
                case GUESSER:
                    return gameEngine.getGameInstanceDataGuessers(gameListingData.getName());
                default:
                    return null;
            }
        }
        else {
            return null;
        }
    }
}
