package input;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class ClientInputController {
    private static final Scanner SCANNER = new Scanner(System.in);
    protected static final int DEFAULT_VALUE = -1;

    protected static Scanner getScanner() { return SCANNER; }

    public static int intMenuInput(final Set<Integer> acceptedIntegers) {
        int input = DEFAULT_VALUE;
        boolean validInput = false;
        while (!validInput) {
            try {
                input = SCANNER.nextInt();
                if (acceptedIntegers.contains(input)) {
                    validInput = true;
                } else {
                    System.out.println("Invalid key, please enter one of the following numbers to select your option: " + acceptedIntegers);
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input: not a number, please enter a number");
                SCANNER.nextLine();
            }
        }
        return input;
    }
}
