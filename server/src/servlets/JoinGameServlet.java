package servlets;

import com.google.gson.JsonSyntaxException;
import exceptions.JoinGameException;
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
    private static final String NOT_EXIST_ERROR = "NOT_EXIST";

    private static final String DEFINERS_NAME = "DEFINER";
    private static final String GUESSERS_NAME = "GUESSER";

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            try {
                checkIfUserInAGame(req);
                final PlayerState playerState = JSONUtils.fromJson(ServletUtils.getRequestBody(req), PlayerState.class);

            } catch (JoinGameException e) {
                ResponseUtils.sendJoinGameError(res, e);
            } catch (JsonSyntaxException e) {
                final String message = "The request body has bad or unexpected JSON syntax.\n" + e.getMessage();
                ResponseUtils.sendJoinGameError(res, BAD_JSON_ERROR, message, HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }






//
//                final String jsonBody = ServletUtils.getRequestBody(req);
//                try {
//                    final PlayerState playerState = JSONUtils.fromJson(jsonBody, PlayerState.class);
//                    final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
//                    if (isGameAvailable(playerState)) {
//                        if (isTeamAvailable(playerState)) {
//                            if (isRoleAvailable(playerState)) {
//
//                            }
//                        }
//                    }
//                    checkIfGameIsFull(res, playerState);
//
//                } catch (JsonSyntaxException e) {
//
//                    ResponseUtils.sendJoinGameError(res, BAD_JSON_ERROR, message, HttpServletResponse.SC_BAD_REQUEST);
//                }
    }

    private void checkIfUserInAGame(final HttpServletRequest req) throws JoinGameException {
        final PlayerState playerState = SessionUtils.getPlayerState(req);
        if (playerState != null) {
            final String message = "This user has already joined game \"" + playerState.getGame() + "\".";
            throw new JoinGameException(ALREADY_JOINED_GAME_ERROR, message, HttpServletResponse.SC_CONFLICT);
        }
    }

    private void checkJoinRequestAvailability(final PlayerState playerState) throws JoinGameException {
        final LobbyManager
    }

    private void checkIfGameAvailable(final String gameName, final LobbyManager lobbyManager) throws JoinGameException {

    }

    private void checkIfTeamAvailable(final String teamName, final GameListingData game, final LobbyManager lobbyManager) throws JoinGameException {

    }

    private void checkIfRoleAvilable(final )

    private boolean aaaaisGameAvailable(final PlayerState playerState, final LobbyManager lobbyManager) {
        final GameListingData gameListing = lobbyManager.getGameListing(playerState.getGame());
        // Check if the game is not full, equivalent to check if the game is still pending.
        if (gameListing != null) {
            if (gameListing.isGamePending()) {
                final Team team = gameListing.getTeam(playerState.getTeam());
                // Check if the team is not full.
                if (!gameListing.isTeamFull(team)) {

                } else {
                    final String message = "The requested team \"" + playerState.getTeam() + "\" is full. Please select a different team.";
                    ResponseUtils.sendJoinGameError(res, TEAM_FULL_ERROR, message, HttpServletResponse.SC_CONFLICT);
                }
            } else {
                final String message = "The requested game \"" + playerState.getGame() + "\" is full and has already started. Please select a different game.";
                ResponseUtils.sendJoinGameError(res, GAME_FULL_ERROR, message, HttpServletResponse.SC_CONFLICT);
            }
        }
        else {
            ResponseUtils.sendJoinGameError(res, NOT_EXIST_ERROR, );
        }
    }

    private boolean isGameAvailable(final PlayerState playerState, final LobbyManager lobbyManager) {
        return lobbyManager.isGameAvailable(playerState.getGame());
    }

    private boolean isTeamAvailable(final PlayerState playerState, final LobbyManager lobbyManager) {
        return lobbyManager.isTeamAvailable(playerState.getGame(), playerState.getTeam());
    }

    private boolean isRoleAvailable(final PlayerState playerState, final LobbyManager lobbyManager) {
        return lobbyManager.isRoleAvailable(playerState.getGame(), playerState.getTeam(), playerState.getRole());
    }
}
