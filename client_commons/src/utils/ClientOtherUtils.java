package utils;

import input.ClientInputController;
import login.ClientLoginController;
import ui.ClientUIElements;
import utils.http.ClientHttpClientUtils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientOtherUtils {
    public static void exitProgram() {
        ClientLoginController.logout();
        ClientUIElements.exitMessage();
        ClientInputController.closeScanner();
        ClientHttpClientUtils.closeHttpClient();
    }

    public static Set<Integer> makeSetFromOneToN(final int n) {
        return IntStream.rangeClosed(1, n).boxed().collect(Collectors.toSet());
    }
}
