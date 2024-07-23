package application;

import input.InputController;
import login.LoginController;
import ui.UIElements;
import utils.OtherUtils;

public class PlayerApplication {
    private static boolean exitingProgram = false;
    public static void main(String[] args) {
        UIElements.greeter();
        LoginController.loginToServer();
        while (!exitingProgram) {
            UIElements.printMainMenu();
            InputController.mainMenuSelection();
        }
        OtherUtils.exitProgram();
    }

    public static void tellToExit() { exitingProgram = true; }
}
