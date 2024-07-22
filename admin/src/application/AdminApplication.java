package application;

import input.InputController;
import login.exceptions.UsernameInputException;
import ui.ClientUIElements;
import ui.UIElements;
import utils.http.ClientHttpClientUtils;

import java.io.IOException;

public class AdminApplication {
    private static final String ADMIN_USERNAME = "admin";
    private static boolean exitingProgram = false;
    public static void main(String[] args) {
        ClientUIElements.greeter();
        loginToServer();
        while (!exitingProgram) {
            UIElements.printMainMenu();
            InputController.mainMenuSelection();
        }
        exitProgram();
    }

    public static void tellToExit() {
        exitingProgram = true;
    }

    private static void loginToServer() {
        boolean loginSuccess = false;
        while (!loginSuccess) {
            try {
                ClientUIElements.connectingToServerMessage();
                ClientHttpClientUtils.AttemptToLogin(ADMIN_USERNAME);
                ClientUIElements.userLoggedInMessage(ADMIN_USERNAME);
                loginSuccess = true;
            } catch (IOException e) {
                ClientUIElements.unexpectedIOExceptionMessage(e);
            } catch (UsernameInputException e) {
                // Admin has already logged on
                UIElements.adminAlreadyJoinedMessage();
                ClientUIElements.pressEnterToContinue();
            }
        }
    }

    private static void logout() {
        boolean logoutSuccess = false;
        while (!logoutSuccess) {
            try {
                ClientUIElements.logoutMessage();
                ClientHttpClientUtils.LogoutFromServer();
                logoutSuccess = true;
            }
            catch (IOException e) {
                ClientUIElements.unexpectedIOExceptionMessage(e);
            }
        }
    }

    private static void exitProgram() {
        logout();
        ClientHttpClientUtils.closeHttpClient();
        ClientUIElements.exitMessage();
    }
}
