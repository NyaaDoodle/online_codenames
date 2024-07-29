package client.input;

import client.ui.ClientUIElements;
import client.utils.constants.ClientConstants;

import java.util.Scanner;
import java.util.Set;

public class ClientInputController {
    private static final Scanner SCANNER = new Scanner(System.in);

    protected static Scanner getScanner() { return SCANNER; }

    public static void closeScanner() { SCANNER.close(); }

    public static int intMenuInputRegular(final Set<Integer> acceptedIntegers) {
        return intMenuInput(acceptedIntegers, false, false);
    }

    public static int intMenuInputWithBack(final Set<Integer> acceptedIntegers) {
        return intMenuInput(acceptedIntegers, true, false);
    }

    public static int intMenuInputWithQuit(final Set<Integer> acceptedIntegers) {
        return intMenuInput(acceptedIntegers, false, true);
    }

    private static int intMenuInput(final Set<Integer> acceptedIntegers, final boolean withBack, final boolean withQuit) {
        int input = ClientConstants.ERROR_NUM;
        String rawInput;
        boolean validInput = false;
        boolean exitLoop = false;
        while (!validInput && !exitLoop) {
            rawInput = SCANNER.nextLine();
            if (withBack && rawInput.equals(ClientConstants.GO_BACK_STRING)) {
                input = ClientConstants.GO_BACK_NUM;
                exitLoop = true;
            }
            if (withQuit && rawInput.equals(ClientConstants.QUIT_STRING)) {
                input = ClientConstants.QUIT_NUM;
                exitLoop = true;
            }
            if (!exitLoop) {
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

    public static String chatMessageInput() {
        while (true) {
            final String input = SCANNER.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input;
            }
            else {
                System.out.println("Chat message cannot be empty or only white-space.");
            }
        }
    }
}
