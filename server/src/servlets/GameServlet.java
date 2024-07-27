package servlets;

import game.engine.GameEngine;
import game.instance.Hint;
import game.instance.MoveEvent;
import game.instance.data.GameInstanceData;
import game.structure.Team;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.LobbyManager;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import users.PlayerStateManager;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;
import utils.json.JSONUtils;

import java.io.IOException;

@WebServlet(name = Constants.GAME_SERVLET_NAME, urlPatterns = {Constants.GAME_RESOURCE_URI})
public class GameServlet extends HttpServlet {
    private static final String HINT_WORDS_PARAMETER_NAME = "hint-words";
    private static final String GUESSES_COUNT_PARAMETER_NAME = "number";
    private static final String CARD_NUMBER_PARAMETER_NAME = "card-number";
    private static final String QUIT_GUESSING = "END";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        String message;
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final String username = SessionUtils.getUsername(req);
            final PlayerStateManager playerStateManager = ServletUtils.getPlayerStateManager(getServletContext());
            final PlayerState playerState = playerStateManager.getPlayerState(username);
            synchronized (this) {
                if (playerState != null) {
                    final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
                    if (lobbyManager.isGameActive(playerState.getGame())) {
                        final GameEngine gameEngine = ServletUtils.getGameEngine(getServletContext());
                        final String gameName = playerState.getGame();
                        final GameInstanceData gameInstanceData = gameEngine.getGameInstanceDataFull(gameName);
                        final Team currentTeam = gameInstanceData.getCurrentTurn();
                        if (!currentTeam.getName().equals(playerState.getTeam())) {
                            final GameRole currentRole = gameInstanceData.getCurrentRole();
                            if (currentRole.equals(playerState.getRole())) {
                                try {
                                    switch (playerState.getRole()) {
                                        case DEFINER:
                                            final Hint hint = parseHintFromBody(req);
                                            gameEngine.setHintAtGame(gameName, hint);
                                            res.setStatus(HttpServletResponse.SC_OK);
                                            break;
                                        case GUESSER:
                                            final int cardIndex = parseCardNumberFromBody(req, gameInstanceData);
                                            if (cardIndex == Constants.QUIT_NUM) {
                                                gameEngine.endTurnAtGame(gameName);
                                                res.setStatus(HttpServletResponse.SC_OK);
                                            } else {
                                                if (!gameEngine.isCardFoundAtGame(gameName, cardIndex)) {
                                                    if (gameInstanceData.getGuessesLeft() > 0) {
                                                        final MoveEvent moveEvent = gameEngine.makeMoveAtGame(gameName, cardIndex);
                                                        if (moveEvent != null) {
                                                            if (gameEngine.hasGameEnded(gameName)) {
                                                                endGame(gameName, gameEngine, lobbyManager, playerStateManager);
                                                            } else if (!moveEvent.getTeamsThatLeftPlay().isEmpty()) {
                                                                moveEvent.getTeamsThatLeftPlay().forEach(team -> playerStateManager.nullifyPlayerStateByteam(team.getName()));
                                                            }
                                                            sendMoveEvent(res, moveEvent);
                                                        } else {
                                                            throw new ServletException("The performed move was illegal - moveEvent was null");
                                                        }
                                                    } else {
                                                        message = "Your team has run out of guesses. The turn has moved on to the next team.";
                                                        ResponseUtils.sendPlainTextConflict(res, message);
                                                    }
                                                } else {
                                                    message = "This card has already been found. Select a different card.";
                                                    ResponseUtils.sendPlainTextConflict(res, message);
                                                }
                                            }
                                            break;
                                    }
                                } catch (ServletException | IOException e) {
                                    throw e;
                                } catch (Exception e) {
                                    ResponseUtils.sendPlainTextBadRequest(res, e.getMessage());
                                }
                            } else {
                                message = "It is not the " + ((playerState.getRole().equals(GameRole.DEFINER)) ? "definers'" : "guessers'") + "turn yet or it has ended.";
                                ResponseUtils.sendPlainTextConflict(res, message);
                            }
                        } else {
                            message = "It is not your team's turn yet or it has ended. Current team: \"" + currentTeam.getName() + "\"";
                            ResponseUtils.sendPlainTextConflict(res, message);
                        }
                    } else {
                        message = "The game isn't active yet. Please wait until all players have connected.";
                        ResponseUtils.sendPlainTextConflict(res, message);
                    }
                } else {
                    ResponseUtils.sendNoPlayerStatusError(res);
                }
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }

    private Hint parseHintFromBody(final HttpServletRequest req) throws Exception {
        final String hintWords = req.getParameter(HINT_WORDS_PARAMETER_NAME);
        final String numberRaw = req.getParameter(GUESSES_COUNT_PARAMETER_NAME);
        int number;
        try {
            number = Integer.parseInt(numberRaw);
        } catch (NumberFormatException e) {
            throw new Exception("The hint number input specified was not a number");
        }
        return new Hint(hintWords, number);
    }

    private int parseCardNumberFromBody(final HttpServletRequest req, final GameInstanceData gameInstanceData) throws Exception {
        final String cardNumberRaw = req.getParameter(CARD_NUMBER_PARAMETER_NAME);
        if (cardNumberRaw.equals(QUIT_GUESSING)) {
            return Constants.QUIT_NUM;
        }
        else {
            try {
                final int index = Integer.parseInt(cardNumberRaw) - 1;
                if (isIndexInBounds(index, gameInstanceData)) {
                    return index;
                }
                else {
                    throw new Exception("The card index specified is out of bounds. Please select a number between 1 and "
                            + gameInstanceData.getWordCards().size());
                }
            } catch (NumberFormatException e) {
                throw new Exception("The card index input specified was not a number.");
            }
        }
    }

    private boolean isIndexInBounds(final int cardIndex, final GameInstanceData gameInstanceData) {
        // This takes the "real" index, instead of the one the user sees.
        return (cardIndex >= 0) && (cardIndex < gameInstanceData.getWordCards().size());
    }

    private void endGame(final String gameName, final GameEngine gameEngine, final LobbyManager lobbyManager, final PlayerStateManager playerStateManager) {
        gameEngine.removeGame(gameName);
        lobbyManager.resetGame(gameName);
        playerStateManager.nullifyPlayerStateByGame(gameName);
    }

    private void sendMoveEvent(final HttpServletResponse res, final MoveEvent moveEvent) throws IOException {
        final String jsonBody = JSONUtils.toJson(moveEvent);
        ResponseUtils.sendJSONSuccess(res, jsonBody);
    }
}
