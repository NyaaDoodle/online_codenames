package servlets;

import com.google.gson.JsonSyntaxException;
import exceptions.JoinGameException;
import game.engine.GameEngine;
import game.structure.Team;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.LobbyManager;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;
import users.PlayerStateManager;
import utils.LogUtils;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;
import java.util.Objects;

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
                    final String username = SessionUtils.getUsername(req);
                    assert username != null;
                    final PlayerStateManager playerStateManager = ServletUtils.getPlayerStateManager(getServletContext());
                    checkIfUserInAGame(username, playerStateManager);
                    final PlayerState playerState = JSONUtils.fromJson(ServletUtils.getRequestBody(req), PlayerState.class);
                    final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
                    final boolean isAdmin = Constants.ADMIN_USERNAMES.contains(username);
                    if (isAdmin) {
                        logToConsole(username, playerState);
                        joinGameAsAdmin(playerState, username, playerStateManager);
                        res.setStatus(HttpServletResponse.SC_OK);
                    }
                    else {
                        checkJoinRequestAvailability(playerState, lobbyManager);
                        logToConsole(username, playerState);
                        joinGame(playerState, username, lobbyManager, playerStateManager);
                        checkIfGameIsFull(playerState.getGame(), lobbyManager);
                        res.setStatus(HttpServletResponse.SC_OK);
                    }
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

    private void checkIfUserInAGame(final String username, final PlayerStateManager playerStateManager) throws JoinGameException {
        final PlayerState playerState = playerStateManager.getPlayerState(username);
        if (playerState != null) {
            final String message = "This user has already joined game \"" + playerState.getGame() + "\".";
            throw new JoinGameException(ALREADY_JOINED_GAME_ERROR, message, HttpServletResponse.SC_CONFLICT);
        }
    }

    private void checkJoinRequestAvailability(final PlayerState playerState, final LobbyManager lobbyManager) throws JoinGameException {
        final GameListingData game = lobbyManager.getGameListing(playerState.getGame());
        Team team = (game != null) ? game.getTeam(playerState.getTeam()) : null;
        final GameRole role = playerState.getRole();
        String errorMessage;
        // Validity check
        if (game == null) {
            errorMessage = "The requested game \"" + playerState.getGame() + "\" does not exist in the server.";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else if (team == null) {
            errorMessage = "The requested team \"" + playerState.getTeam() + "\" does not exist in the game \"" + playerState.getGame() + "\".";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_CONFLICT);
        }
        else if (role == null) {
            errorMessage = "Invalid role: \"" + playerState.getRole().toString() +"\".";
            throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
        }
        // Availability check
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
                    errorMessage = "Invalid role: \"" + playerState.getRole().toString() + "\".";
                    throw new JoinGameException(NOT_EXIST_ERROR, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void joinGame(final PlayerState playerState, final String username, final LobbyManager lobbyManager, final PlayerStateManager playerStateManager) {
        lobbyManager.joinGame(playerState);
        playerStateManager.setPlayerState(username, playerState);
    }

    private void joinGameAsAdmin(final PlayerState playerState, final String username, final PlayerStateManager playerStateManager) {
        playerStateManager.setPlayerState(username, playerState);
    }

    private void checkIfGameIsFull(final String gameName, final LobbyManager lobbyManager) {
        if (lobbyManager.isGameActive(gameName)) {
            tellToCreateGameInstance(Objects.requireNonNull(lobbyManager.getGameListing(gameName)));
        }
    }

    private void tellToCreateGameInstance(final GameListingData game) {
        final GameEngine gameEngine = ServletUtils.getGameEngine(getServletContext());
        LogUtils.logToConsole("Creating game instance of \"" + game.getName() + "\"");
        gameEngine.addGame(game);
    }

    private void logToConsole(@NotNull final String username, @NotNull final PlayerState playerState) {
        LogUtils.logToConsole("Adding user \"" + username + "\" to game \"" + playerState.getGame() + "\""
                + " in team \"" + playerState.getTeam() + "\" as a " + (playerState.getRole().equals(GameRole.DEFINER) ? "definer" : "guesser"));
    }
}
