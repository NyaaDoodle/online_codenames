package input;

import application.AdminApplication;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputController extends ClientInputController {
    private static final Scanner SCANNER = getScanner();
    private static final int OPTIONS_IN_MAIN_MENU = 4;
    private static final Set<Integer> INTEGERS_FOR_MAIN_MENU
            = IntStream.rangeClosed(1, OPTIONS_IN_MAIN_MENU).boxed().collect(Collectors.toSet());

    public static void mainMenuSelection() {
        int input = ClientInputController.intMenuInput(INTEGERS_FOR_MAIN_MENU);
        switch (input) {
            case 1:
                // TODO add game
                break;
            case 2:
                // TODO list games
                break;
            case 3:
                // TODO spectate game - after player-client work
                break;
            case 4:
                AdminApplication.tellToExit();
                break;
        }
    }

    public static void fileNameInput(final Set<String> acceptedFileTypes) {

    }
}
