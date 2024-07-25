package servlets;

import com.google.gson.JsonSyntaxException;
import exceptions.JoinGameException;
import game.structure.Team;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.LobbyManager;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import lobby.game.join.PlayerStateIdentifiers;
import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;
import utils.LogUtils;
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

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            try {
                synchronized (this) {
                    checkIfUserInAGame(req);
                    final PlayerStateIdentifiers playerStateIdentifiers = JSONUtils.fromJson(ServletUtils.getRequestBody(req), PlayerStateIdentifiers.class);
                    final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
                    final PlayerState playerState = PlayerState.createFromPlayerStateIdentifiers(playerStateIdentifiers, lobbyManager);
                    checkPlayerStateValidity(playerState);
                    checkJoinRequestAvailability(playerState, lobbyManager);
                    LogUtils.logToConsole("Adding user \"" + SessionUtils.getUsername(req) + "\" to game \"" + playerState.getGameName() + "\""
                    + " in team \"" + playerState.getTeamName() + "\" as a " + (playerState.getRole().equals(GameRole.DEFINER) ? "definer" : "guesser"));
                    lobbyManager.joinGame(playerState);
                    SessionUtils.setPlayerState(req, playerState);
                    res.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (JoinGameException e) {
                ResponseUtils.sendJoinGameError(res, e);
            } catch (JsonSyntaxException e) {
                final String message = "The request body has bad or unexpected JSON syntax.\n" + e.getMessage();
                ResponseUtils.sendJoinGameError(res, BAD_JSON_ERROR, message, HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            ResponseUtils.sendUnauthorized(res);
        }

    }

    private void checkIfUserInAGame(final HttpServletRequest req) throws JoinGameException {
        final PlayerState playerState = SessionUtils.getPlayerState(req);
        if (playerState != null) {
            final String message = "This user has already joined game \"" + playerState.getGameName() + "\".";
            throw new JoinGameException(ALREADY_JOINED_GAME_ERROR, message, HttpServletResponse.SC_CONFLICT);
        }
    }

    private void checkPlayerStateValidity(final PlayerState playerState) throws JoinGameException {
        String errorMessage;
        if (playerState.getGame() == null) {
            errorMessage = "The requested game \"" + playerState.getGameName() + "\" does not exist in the server.";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else if (playerState.getTeam() == null) {
            errorMessage = "The requested team \"" + playerState.getTeamName() + "\" does not exist in the game \"" + playerState.getGameName() + "\".";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else if (playerState.getRole() == null) {
            errorMessage = "Invalid role: \"" + playerState.getRole().toString() +"\".";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void checkJoinRequestAvailability(final PlayerState playerState, final LobbyManager lobbyManager) throws JoinGameException {
        // Assuming playerState has already been checked for validity of nulls.
        @NotNull final GameListingData game = playerState.getGame();
        @NotNull final Team team = playerState.getTeam();
        @NotNull final GameRole role = playerState.getRole();
        String errorMessage;
        if (game.isGameActive()) {
            errorMessage = "The requested game \"" + game.getName() + "\" is full and has already started. Please select a different game.";
            throw new JoinGameException(GAME_FULL_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else if (game.isTeamFull(team)) {
            errorMessage = "The requested team \"" + team.getName() + "\" is full. Please select a different team.";
            throw new JoinGameException(TEAM_FULL_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else {
            switch (role) {
                case DEFINER:
                    if (game.areDefinersFull(team)) {
                        errorMessage = "The definers team is full. Please select a different team.";
                        throw new JoinGameException(ROLE_FULL_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
                    }
                    break;
                case GUESSER:
                    if (game.areGuessersFull(team)) {
                        errorMessage = "The guessers team is full. Please select a different team.";
                        throw new JoinGameException(ROLE_FULL_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
                    }
                    break;
                default:
                    errorMessage = "Invalid role: \"" + role + "\".";
                    throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
