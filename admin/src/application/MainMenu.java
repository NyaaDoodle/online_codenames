package application;

import input.InputControllers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainMenu {
    private static final int OPTIONS_IN_MAIN_MENU = 4;
    private static final Set<Integer> INTEGERS_FOR_MAIN_MENU
            = IntStream.rangeClosed(1, OPTIONS_IN_MAIN_MENU).boxed().collect(Collectors.toSet());

    public static void printMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Load a new game");
        System.out.println("(2) Show game listings");
        System.out.println("(3) Spectate on an active game");
        System.out.println("(4) Logout");
    }

    public static void selection() {
        int input = InputControllers.intMenuInput(INTEGERS_FOR_MAIN_MENU);
        switch (input) {
            case 1:
                // TODO add game
                break;
            case 2:
                // TODO list games
                break;
            case 3:
                // TODO spectate game - after player-client work
                break;
            case 4:
                AdminApplication.tellToExit();
                break;
        }
    }
}
