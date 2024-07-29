package input;

import client.game.instance.GameRole;
import client.input.ClientInputController;
import game.join.GameJoiner;
import application.PlayerApplication;
import game.room.GameRoom;
import game.room.chat.ChatRoomType;
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
    private static final int OPTIONS_FOR_GUESSERS = 3;
    private static final int OPTIONS_FOR_DEFINERS = 4;

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
        final int optionsCount = (gameRoom.getPlayerState().getRole().equals(GameRole.DEFINER)) ? OPTIONS_FOR_DEFINERS : OPTIONS_FOR_GUESSERS;
        int input = intMenuInputRegular(OtherUtils.makeSetFromOneToN(optionsCount));
        gameRoom.updateGameData();
        if (!gameRoom.hasGameEnded()) {
            switch (input) {
                case 1:
                    UIElements.printGameData(gameRoom.getGameData(), gameRoom.getPlayerState().getRole());
                    break;
                case 2:
                    if (gameRoom.getGameData().getGameListingData().isGameActive()) {
                        gameRoom.makeMove();
                    }
                    else {
                        System.out.println("The game hasn't started yet!");
                    }
                    break;
                case 3:
                    gameRoom.goToChatRoom(ChatRoomType.ALL_TEAM_CHAT);
                    break;
                case 4:
                    if (gameRoom.getPlayerState().getRole().equals(GameRole.DEFINER)) {
                        gameRoom.goToChatRoom(ChatRoomType.DEFINERS_CHAT);
                    }
                    else {
                        System.out.println("Guessers are not allowed in the definers-only chatroom.");
                    }
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
