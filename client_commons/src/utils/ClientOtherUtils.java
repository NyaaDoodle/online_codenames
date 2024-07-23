package utils;

import input.ClientInputController;
import login.ClientLoginController;
import ui.ClientUIElements;
import utils.http.ClientHttpClientUtils;

public class ClientOtherUtils {
    public static void exitProgram() {
        ClientLoginController.logout();
        ClientUIElements.exitMessage();
        ClientInputController.closeScanner();
        ClientHttpClientUtils.closeHttpClient();
    }
}
