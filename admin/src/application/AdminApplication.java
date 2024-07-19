package application;

import login.LoginController;

public class AdminApplication {
    private static boolean toExitProgram = false;
    public static void main(String[] args) {
        ConsoleUIElements.Greeter();
        ConsoleUIElements.ConnectingToServerMessage();
        while (!toExitProgram) {

        }
    }
    public static void LoginToServer() {
        try {
            LoginController.AttemptToConnect();
        }
    }

}
