package application;

import java.util.Scanner;

public class PlayerApplication {

    public static void AskForUsernameMessage() {
        System.out.println("Please enter your username: ");
    }

    private static String getUsernameInput() {
        AskForUsernameMessage();
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        scanner.close();
        return username.trim().toLowerCase();
    }
}
