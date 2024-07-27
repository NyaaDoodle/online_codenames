package game.room;

import client.exceptions.NoPlayerStatusAtServerException;
import client.game.data.GameData;
import client.game.instance.GameRole;
import client.game.instance.Hint;
import client.game.instance.MoveEvent;
import client.game.instance.data.GameInstanceData;
import client.game.room.ClientGameRoom;
import client.game.room.PlayerState;
import client.game.structure.Team;
import client.utils.constants.ClientConstants;
import client.utils.http.ClientHttpClientUtils;
import input.InputController;
import client.lobby.game.list.GameListingData;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.OtherUtils;
import utils.json.JSONUtils;

import java.util.Objects;

public class GameRoom extends ClientGameRoom {
    private static final String HINT_WORDS_PARAMETER_NAME = "hint-words";
    private static final String GUESSES_COUNT_PARAMETER_NAME = "number";
    private static final String CARD_NUMBER_PARAMETER_NAME = "card-number";
    private static final String QUIT_GUESSING = "END";
    private static final String GAME_URL = ClientConstants.BASE_URL + ClientConstants.GAME_RESOURCE_URI;

    public GameRoom(@NotNull GameListingData gameListingData, @NotNull PlayerState playerState) {
        super(gameListingData, playerState);
    }

    public void makeMove() {
        updateGameData();
        final GameData gameData = getGameData();
        final PlayerState playerState = getPlayerState();
        if (gameData.getGameListingData().isGameActive()) {
            final GameInstanceData gameInstanceData = gameData.getGameInstanceData();
            assert gameInstanceData != null;
            final Team currentTeam = gameInstanceData.getCurrentTurn();
            final GameRole currentRole = gameInstanceData.getCurrentRole();
            if (currentTeam.getName().equals(playerState.getTeam())) {
                HttpUrl finalUrl = null;
                if (currentRole.equals(playerState.getRole())) {
                    final int upperLimit = getGameData().getGameInstanceData().getWordCards().size();
                    switch (getPlayerState().getRole()) {
                        case DEFINER:
                            System.out.println("Please enter the hint phrase:");
                            final String hintWords = InputController.getStringTrimmed();
                            System.out.println("Please enter the number of guesses:");
                            final int hintNumber = InputController.intMenuInput(OtherUtils.makeSetFromOneToN(upperLimit));
                            finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_URL)).newBuilder()
                                    .addQueryParameter(HINT_WORDS_PARAMETER_NAME, hintWords)
                                    .addQueryParameter(GUESSES_COUNT_PARAMETER_NAME, String.valueOf(hintNumber))
                                    .build();
                            break;
                        case GUESSER:
                            final Hint currentHint = gameInstanceData.getCurrentHint();
                            UIElements.printBoard(getGameData().getGameInstanceData(), GameRole.GUESSER);
                            System.out.println("The current hint: \"" + currentHint.getHintWords() + "\" , " + currentHint.getNumber());
                            System.out.println("Please enter the number under each card to select the word:");
                            int cardNumber = ClientConstants.ERROR_NUM;
                            boolean validInput = false;
                            while (!validInput) {
                                cardNumber = InputController.intMenuInput(OtherUtils.makeSetFromOneToN(upperLimit));
                                if (!gameInstanceData.isCardFound(cardNumber - 1)) {
                                    validInput = true;
                                }
                                else {
                                    System.out.println("This card has already been found. Select a different card.");
                                }
                            }
                            finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_URL)).newBuilder().
                                    addQueryParameter(CARD_NUMBER_PARAMETER_NAME, String.valueOf(cardNumber))
                                    .build();
                            break;
                    }
                    final Request req = new Request.Builder().get().url(finalUrl).build();
                    try {
                        final MoveEvent moveEvent = JSONUtils.fromJson(ClientHttpClientUtils.sendGameRequest(req), MoveEvent.class);
                        // TODO look at the moveEvent
                    } catch (NoPlayerStatusAtServerException e) {
                        System.out.println(e.getMessage());
                        setGameEnded();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    System.out.println("It is not the " + ((playerState.getRole().equals(GameRole.DEFINER)) ? "definers'" : "guessers'") + "turn yet or it has ended.");
                }
            }
            else {
                System.out.println("It is not your team's turn yet or it has ended. Current team: \"" + currentTeam.getName() + "\"");
            }
        }
        else {
            System.out.println("The game isn't active yet. Please wait until all players have connected.");
        }
    }

    @Override
    protected void printGameRoomMenu() {
        // TODO add chat, differ between definer and guesser
        System.out.println("Select an option:");
        System.out.println("(1) Retrieve current game status");
        System.out.println("(2) Perform a move");
        System.out.println("(3) ");
    }

    @Override
    protected void gameRoomMenuSelection() {
        InputController.gameRoomMenuSelection(this);
    }

    private void checkMoveEvent(final MoveEvent moveEvent) {
        final boolean isBlackCard = moveEvent.isBlackCard();
        final boolean isNeutralCard = moveEvent.isNeutralCard();
        final boolean hasGameEnded = moveEvent.isHasGameEnded();
        final String cardTeam = moveEvent.getCardTeam().getName();
        final String selectingTeam = moveEvent.getSelectingTeam().getName();
        // TODO continue this later...

    }
}
