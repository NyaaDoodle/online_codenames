package input;

import ui.ClientUIElements;
import utils.constants.ClientConstants;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class ClientInputController {
    private static final Scanner SCANNER = new Scanner(System.in);

    protected static Scanner getScanner() { return SCANNER; }

    public static void closeScanner() { SCANNER.close(); }

    public static int intMenuInput(final Set<Integer> acceptedIntegers) {
        int input = ClientConstants.ERROR_NUM;
        String rawInput;
        boolean validInput = false;
        boolean goBack = false;
        while (!validInput && !goBack) {
            rawInput = SCANNER.nextLine();
            if (rawInput.equals(ClientConstants.GO_BACK_STRING)) {
                input = ClientConstants.GO_BACK_NUM;
                goBack = true;
            }
            else {
                try {
                    input = Integer.parseInt(rawInput);
                    if (acceptedIntegers.contains(input)) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid key, please enter one of the following numbers to select your option: " + acceptedIntegers);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: not a number, please enter a number");
                } catch (Exception e) {
                    ClientUIElements.unexpectedExceptionMessage(e);
                }
            }
        }
        return input;
    }
}
