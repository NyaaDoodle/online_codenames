package login;

import login.exceptions.UsernameInputException;
import ui.UIElements;
import utils.http.HttpClientUtils;

import java.io.IOException;

public class LoginController extends ClientLoginController {
    private static final String ADMIN_USERNAME = "admin";
    public static void loginToServer() {
        boolean loginSuccess = false;
        while (!loginSuccess) {
            try {
                UIElements.connectingToServerMessage();
                HttpClientUtils.attemptToLogin(ADMIN_USERNAME);
                UIElements.userLoggedInMessage(ADMIN_USERNAME);
                loginSuccess = true;
            } catch (IOException e) {
                UIElements.unexpectedIOExceptionMessage(e);
            } catch (UsernameInputException e) {
                // Admin has already logged on
                UIElements.adminAlreadyJoinedMessage();
                UIElements.pressEnterToContinue();
            }
        }
    }
}
