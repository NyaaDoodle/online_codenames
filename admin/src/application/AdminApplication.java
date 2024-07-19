package application;

import login.exceptions.UsernameInputException;
import ui.ClientUIElements;
import util.http.HttpClientUtils;

import java.io.IOException;

public class AdminApplication {
    private static final String ADMIN_USERNAME = "admin";
    //private static boolean toExitProgram = false;
    public static void main(String[] args) {
        ClientUIElements.greeter();
        loginToServer();
        // TODO while not toExitProgram
        exitProgram();
    }

    public static void loginToServer() {
        boolean loginSuccess = false;
        while (!loginSuccess) {
            try {
                ClientUIElements.connectingToServerMessage();
                HttpClientUtils.AttemptToLogin(ADMIN_USERNAME);
                ClientUIElements.userLoggedInMessage(ADMIN_USERNAME);
                loginSuccess = true;
            } catch (IOException e) {
                ClientUIElements.unexpectedIOExceptionMessage(e);
            } catch (UsernameInputException e) {
                // Admin has already logged on
                AdminUIElements.AdminAlreadyJoinedMessage();
                ClientUIElements.pressEnterToContinue();
            }
        }
    }

    public static void logout() {
        boolean logoutSuccess = false;
        while (!logoutSuccess) {
            try {
                ClientUIElements.logoutMessage();
                HttpClientUtils.LogoutFromServer();
                logoutSuccess = true;
            }
            catch (IOException e) {
                ClientUIElements.unexpectedIOExceptionMessage(e);
            }
        }
    }

    public static void exitProgram() {
        logout();
        HttpClientUtils.closeHttpClient();
        ClientUIElements.exitMessage();
    }
}
