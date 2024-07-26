package servlets;

import game.data.GameData;
import game.engine.GameEngine;
import game.instance.Hint;
import game.instance.MoveEvent;
import game.instance.Pair;
import game.instance.data.GameInstanceData;
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
import org.jetbrains.annotations.Nullable;
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
    private static final String CARD_INDEX_PARAMETER_NAME = "card-index";
    private static final String QUIT_GUESSING = "END";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        // TODO am i allowed to send this?
        if (ServletUtils.isUserLoggedIn(req, getServletContext())) {
            final PlayerState playerState = SessionUtils.getPlayerState(req);
            if (playerState != null) {
                final LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
                if (lobbyManager.isGameActive(playerState.getGame())) {
                    final GameEngine gameEngine = ServletUtils.getGameEngine(getServletContext());
                    checkIfOnCorrectTurn(playerState, gameEngine);
                    if (currentRole.equals(playerState.getRole())) {
                        try {
                            switch (playerState.getRole()) {
                                case DEFINER:
                                    final Hint hint = parseHintFromBody(req);
                                    gameEngine.setHintAtGame(playerState.getGame(), hint);
                                    // TODO is this enough?
                                    res.setStatus(HttpServletResponse.SC_OK);
                                    break;
                                case GUESSER:
                                    final int cardIndex = parseCardIndexFromBody(req);
                                    if (cardIndex == Constants.QUIT_NUM) {
                                        // TODO quit guessing
                                    }
                                    else {
                                        if (isIndexInBounds(cardIndex, gameInstanceData)) {
                                            final MoveEvent moveEvent = gameEngine.makeMoveAtGame(playerState.getGame(), cardIndex);
                                            // TODO did the game end, was the moveEvent valid??
                                        }
                                        else {
                                            // TODO out of bounds
                                        }
                                    }
                                    break;
                                default:
                                    // TODO see what to do here...
                                    break;
                            }
                        } catch (Exception e) {
                            // TODO exception
                        }
                    }
                    else {
                        // TODO tell the user it isnt this role's turn yet
                        }
                }
                else {
                    // TODO tell the user it isnt this team's turn yet
                }
                }
                else {
                    // TODO tell the user the game isnt active yet
                }
            }
            else {
                ResponseUtils.sendUnauthorized(res);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }

    private void checkIfOnCorrectTurn(final PlayerState playerState, final GameEngine gameEngine) throws Exception {
        final GameInstanceData gameInstanceData = gameEngine.getGameInstanceDataFull(playerState.getGame());
        final Team currentTeam = gameInstanceData.getCurrentTurn();
        if (!currentTeam.getName().equals(playerState.getTeam())) {
            throw new Exception("It is not team \"" + playerState.getTeam() + "\"'s turn yet."
                    + "Current team: \"" + currentTeam.getName() + "\"");
        }
        else {

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

    private int parseCardIndexFromBody(final HttpServletRequest req) throws Exception {
        final String cardIndexRaw = req.getParameter(CARD_INDEX_PARAMETER_NAME);
        if (cardIndexRaw.equals(QUIT_GUESSING)) {
            return Constants.QUIT_NUM;
        }
        else {
            try {
                return Integer.parseInt(cardIndexRaw) - 1;
            } catch (NumberFormatException e) {
                throw new Exception("The card index input specified was not a number.");
            }
        }
    }

    private boolean isIndexInBounds(final int cardIndex, final GameInstanceData gameInstanceData) {
        // This takes the "real" index, instead of the one the user sees.
        return (cardIndex >= 0) && (cardIndex < gameInstanceData.getWordCards().size());
    }
}
