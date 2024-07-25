package ui;

import game.structure.Team;
import input.ClientInputController;
import lobby.LobbyController;
import lobby.game.list.GameList;
import lobby.game.list.GameListingData;
import utils.constants.ClientConstants;

import java.io.IOException;
import java.util.List;

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

    public static void unexpectedExceptionMessage(final Exception e) {
        System.out.println("Unexpected error happened...");
        System.out.println(e.getMessage());
    }

    public static void pressEnterToContinue() {
        try { System.in.read(); } catch (IOException e) { unexpectedIOExceptionMessage(e); exit(-100); }
    }

    public static void goBackOptionMessage() {
        System.out.println("(Enter \"" + ClientConstants.GO_BACK_STRING + "\" to go back to the previous menu)");
    }

    public static void logoutMessage() {
        System.out.println("Logging off...");
    }

    public static void exitMessage() {
        System.out.println("Exiting...");
    }

    public static void printGameListAll() {
        try {
            GameList gameList = LobbyController.getGameListAll();
            printGameList(gameList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printGameListOnlyActive() {
        try {
            GameList gameList = LobbyController.getGameListOnlyActive();
            printGameList(gameList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printGameListOnlyPending() {
        try {
            GameList gameList = LobbyController.getGameListOnlyPending();
            printGameList(gameList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printGameList(final GameList gameList) {
        System.out.println((gameList.getGameAmount() > 0) ? "Current game listings on the server:" : "No games are available that fit the criteria currently.");
        final List<GameListingData> gameListings = gameList.getGameList();
        for (int i = 0; i < gameListings.size(); i++) {
            final GameListingData game = gameListings.get(i);
            final int listingNum = i + 1;
            System.out.println("(" + listingNum + ")");
            System.out.println("Game name: " + game.getName());
            System.out.println("Status: " + game.getState().toString());
            System.out.println("Board dimensions: " + game.getRows() + " X " + game.getColumns());
            System.out.println("Dictionary file name: " + game.getDictionaryFileName());
            System.out.println("Amount of regular cards in-game: " + game.getRegularCardsCount());
            System.out.println("Amount of black cards in-game: " + game.getBlackCardsCount());
            System.out.println("Teams:");
            for (Team team : game.getTeams()) {
                System.out.println("Team name: " + team.getName());
                System.out.println("Word count: " + team.getCardCount());
                final int definersSignedUp = game.getConnectedDefinersByTeam(team.getName());
                final int guessersSignedUp = game.getConnectedGuessersByTeam(team.getName());
                System.out.println("Definers signed up: " + definersSignedUp + " / " + team.getDefinersCount());
                System.out.println("Guessers signed up: " + guessersSignedUp + " / " + team.getGuessersCount());
            }
            System.out.println();
        }
    }

}
