package servlets;

import com.google.gson.JsonSyntaxException;
import game.structure.Team;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.LobbyManager;
import lobby.game.join.PlayerState;
import lobby.game.list.GameListingData;
import users.UserManager;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;

@WebServlet(name = Constants.JOIN_GAME_SERVLET_NAME, urlPatterns = {Constants.JOIN_GAME_RESOURCE_URI})
public class JoinGameServlet extends HttpServlet {
    private static final String GAME_FULL_ERROR = "GAME_FULL";
    private static final String TEAM_FULL_ERROR = "TEAM_FULL";
    private static final String ROLE_FULL_ERROR = "ROLE_FULL";
    private static final String BAD_JSON_ERROR = "BAD_SYNTAX";
    private static final String ALREADY_JOINED_GAME_ERROR = "ALREADY_JOINED";

    private static final String DEFINERS_NAME = "DEFINER";
    private static final String GUESSERS_NAME = "GUESSER";

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            if (!SessionUtils.isUserInAGame(req)) {
                final String jsonBody = ServletUtils.getRequestBody(req);
                try {
                    final PlayerState playerState = JSONUtils.fromJson(jsonBody, PlayerState.class);
                    checkIfGameIsFull(res, playerState);

                } catch (JsonSyntaxException e) {
                    final String message = "The request body has bad or unexpected JSON syntax.\n" + e.getMessage();
                    ResponseUtils.sendJoinGameError(res, BAD_JSON_ERROR, message, HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else {
                final String message = "This user has already joined a game.";
                ResponseUtils.sendJoinGameError(res, ALREADY_JOINED_GAME_ERROR, message, HttpServletResponse.SC_CONFLICT);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }

    private void checkIfGameIsFull(final HttpServletResponse res, PlayerState playerState) {
        final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
        final GameListingData gameListing = lobbyManager.getGameListing(playerState.getGame());
        // Check if the game is not full, equivalent to check if the game is still pending.
        if (gameListing.isGamePending()) {
            final Team team = gameListing.getTeam(playerState.getTeam());
            // Check if the team is not full.
            if (!gameListing.isTeamFull(team)) {

            }
            else {
                final String message = "The requested team \"" + playerState.getTeam() + "\" is full. Please select a different team.";
                ResponseUtils.sendJoinGameError(res, TEAM_FULL_ERROR, message, HttpServletResponse.SC_CONFLICT);
            }
        }
        else {
            final String message = "The requested game \"" + playerState.getGame() +"\" is full and has already started. Please select a different game.";
            ResponseUtils.sendJoinGameError(res, GAME_FULL_ERROR, message, HttpServletResponse.SC_CONFLICT);
        }
    }
}
