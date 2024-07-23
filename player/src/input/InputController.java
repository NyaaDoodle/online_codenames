package input;

import application.PlayerApplication;
import login.LoginController;
import login.exceptions.UsernameInputException;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class InputController extends ClientInputController {
    private static final Scanner SCANNER = getScanner();

    public static void mainMenuSelection() {
        PlayerApplication.tellToExit();
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
