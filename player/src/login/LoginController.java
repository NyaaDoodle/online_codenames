package login;

import input.InputController;
import login.exceptions.UsernameInputException;
import ui.UIElements;
import utils.http.HttpClientUtils;

import java.io.IOException;

public class LoginController extends ClientLoginController {
    public static void loginToServer() {
        UIElements.AskForUsernameMessage();
        boolean loginSuccess = false;
        while (!loginSuccess) {
            try {
                final String username = InputController.getUsernameInput();
                UIElements.connectingToServerMessage();
                HttpClientUtils.attemptToLogin(username);
                UIElements.userLoggedInMessage(username);
                loginSuccess = true;
            } catch (IOException e) {
                UIElements.unexpectedIOExceptionMessage(e);
            } catch (UsernameInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
