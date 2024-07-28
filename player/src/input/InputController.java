package input;

import client.input.ClientInputController;
import game.join.GameJoiner;
import application.PlayerApplication;
import game.room.GameRoom;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.OtherUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class InputController extends ClientInputController {
    private static final Scanner SCANNER = getScanner();
    private static final int OPTIONS_IN_MAIN_MENU = 3;
    private static final Set<Integer> INTEGERS_FOR_MAIN_MENU
            = OtherUtils.makeSetFromOneToN(OPTIONS_IN_MAIN_MENU);
    private static final int OPTIONS_IN_GAME_ROOM_MENU = 3;
    private static final Set<Integer> INTEGERS_FOR_GAME_ROOM_MENU
            = OtherUtils.makeSetFromOneToN(OPTIONS_IN_GAME_ROOM_MENU);

    private static final Set<String> RESERVED_USERNAMES = new HashSet<>(Collections.singletonList("admin"));

    public static void mainMenuSelection() {
        int input = intMenuInputRegular(INTEGERS_FOR_MAIN_MENU);
        switch (input) {
            case 1:
                UIElements.printGameListAll();
                break;
            case 2:
                GameJoiner.selectGame();
                break;
            case 3:
                PlayerApplication.tellToExit();
                break;
        }
    }

    public static void gameRoomMenuSelection(final GameRoom gameRoom) {
        int input = intMenuInputRegular(INTEGERS_FOR_GAME_ROOM_MENU);
        gameRoom.updateGameData();
        final boolean inGame = !gameRoom.hasGameEnded() && gameRoom.getGameData().getGameListingData().isGameActive();
        if (inGame) {
            switch (input) {
                case 1:
                    UIElements.printGameData(gameRoom.getGameData(), gameRoom.getPlayerState().getRole());
                    break;
                case 2:
                    gameRoom.makeMove();
                    break;
            }
        }
    }

    @NotNull
    public static String getUsernameInput() {
        String input = null;
        boolean validInput = false;
        while (!validInput) {
            input = SCANNER.nextLine().trim().toLowerCase();
            if (!input.isEmpty()) {
                if (!RESERVED_USERNAMES.contains(input)) {
                    validInput = true;
                }
                else {
                    System.out.println("The username \"" + input + "\" is reserved. Please enter a different username.");
                }
            } else {
                System.out.println("A blank username cannot be used.");
            }
        }
        return input;
    }

    @NotNull
    public static String getStringTrimmed() {
        return SCANNER.nextLine().trim();
    }
}
