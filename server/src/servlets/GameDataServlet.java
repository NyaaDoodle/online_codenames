package servlets;

import game.data.GameData;
import game.engine.GameEngine;
import game.instance.data.GameInstanceData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;

@WebServlet(name = Constants.GAME_DATA_SERVLET_NAME, urlPatterns = {Constants.GAME_DATA_RESOURCE_URI})
public class GameDataServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final PlayerState playerState = SessionUtils.getPlayerState(req);
            if (playerState != null) {
                final GameListingData gameListingData = ServletUtils.getLobbyManager(getServletContext()).getGameListing(playerState.getGame());
                final GameInstanceData gameInstanceData = getGameInstanceData(gameListingData, playerState.getRole());
                final GameData gameData = new GameData(gameListingData, gameInstanceData);
                final String jsonBody = JSONUtils.toJson(gameData);
                ResponseUtils.sendJSONSuccess(res, jsonBody);
            }
            else {
                ResponseUtils.sendUnauthorized(res);
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
