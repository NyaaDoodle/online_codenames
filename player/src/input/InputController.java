package input;

import application.GameJoiner;
import application.PlayerApplication;
import login.LoginController;
import login.exceptions.UsernameInputException;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputController extends ClientInputController {
    private static final Scanner SCANNER = getScanner();
    private static final int OPTIONS_IN_MAIN_MENU = 3;
    private static final Set<Integer> INTEGERS_FOR_MAIN_MENU
            = IntStream.rangeClosed(1, OPTIONS_IN_MAIN_MENU).boxed().collect(Collectors.toSet());

    public static void mainMenuSelection() {
        int input = intMenuInput(INTEGERS_FOR_MAIN_MENU);
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

    @NotNull
    public static String getUsernameInput() {
        String input = null;
        boolean validInput = false;
        while (!validInput) {
            input = SCANNER.nextLine().trim().toLowerCase();
            try {
                LoginController.validateUsernameInput(input);
                validInput = true;
            } catch (UsernameInputException e) {
                System.out.println(e.getMessage());
            }
        }
        return input;
    }
}
