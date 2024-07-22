package ui;

public class UIElements extends ClientUIElements {
    public static void adminAlreadyJoinedMessage() {
        System.out.println("The admin user has already logged in to the server. Press enter to try again, or CTRL-C to exit the program.");
    }
    public static void printMainMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Load a new game");
        System.out.println("(2) Show game listings");
        System.out.println("(3) Spectate on an active game");
        System.out.println("(4) Logout");
    }
}
