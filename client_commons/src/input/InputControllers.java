package input;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class InputControllers {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int DEFAULT_VALUE = -1;
    public static int intMenuInput(final Set<Integer> acceptedIntegers) {
        int input = DEFAULT_VALUE;
        boolean validInputAccepted = false;
        while (!validInputAccepted) {
            try {
                input = scanner.nextInt();
                if (acceptedIntegers.contains(input)) {
                    validInputAccepted = true;
                } else {
                    System.out.println("Invalid key, please enter one of the following numbers to select your option: " + acceptedIntegers);
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input: not a number, please enter a number");
                scanner.nextLine();
            }
        }
        return input;
    }
}
