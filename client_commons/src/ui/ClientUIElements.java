package ui;

import java.io.IOException;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class ClientUIElements {
    public static void greeter() {
        System.out.println("Codenames, Version 2");
    }
    public static void connectingToServerMessage() {
        System.out.println("Connecting to game server...");
    }
    public static void userLoggedInMessage(final String username) {
        System.out.println("Successfully logged in as user \"" + username +"\"!");
    }
    public static void unexpectedIOExceptionMessage(final IOException e) {
        System.out.println("Unexpected IO problem happened...");
        System.out.println(e.getMessage());
    }
    public static void pressEnterToContinue() {
        try { System.in.read(); } catch (IOException e) { unexpectedIOExceptionMessage(e); exit(-100); }
    }
    public static void logoutMessage() {
        System.out.println("Logging off...");
    }
    public static void exitMessage() {
        System.out.println("Exiting...");
    }
}
