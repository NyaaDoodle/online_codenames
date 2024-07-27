package ui;

import client.ui.ClientUIElements;

public class UIElements extends ClientUIElements {
    public static void AskForUsernameMessage() {
        System.out.println("Please enter your username: ");
    }

    public static void printMainMenu() {
        System.out.println("Select an option:");
        System.out.println("(1) Show game listings");
        System.out.println("(2) Join a game");
        System.out.println("(3) Logout");
    }
}
