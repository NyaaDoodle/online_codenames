package client.ui;

import client.game.data.GameData;
import client.game.instance.GameRole;
import client.game.instance.data.GameInstanceData;
import client.game.instance.data.WordCardData;
import client.game.structure.Board;
import client.game.structure.GameStructure;
import client.game.structure.Team;
import client.lobby.LobbyController;
import client.lobby.game.list.GameList;
import client.lobby.game.list.GameListingData;
import client.utils.constants.ClientConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;

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

    public static void printGameData(@NotNull final GameData gameData, @NotNull final GameRole gameRole) {
        @NotNull final GameListingData gameListingData = gameData.getGameListingData();
        @Nullable final GameInstanceData gameInstanceData = gameData.getGameInstanceData();
        System.out.println("Current state of game \"" + gameListingData.getName() + "\":");
        System.out.println("Status: " + gameListingData.getState().toString());
        if (gameInstanceData != null) {
            final Team currentTeam = gameInstanceData.getCurrentTurn();
            final Team nextTeam = gameInstanceData.getNextTurn();
            final GameRole currentRole = gameInstanceData.getCurrentRole();
            final int currentScore = gameInstanceData.getTeamScore(currentTeam.getName());
            final int numberOfTurns = gameInstanceData.getTurnCountByTeam(currentTeam.getName());
            System.out.println("Current turn: Team \"" + currentTeam.getName() + "\", "
            + " at " + (currentRole.equals(GameRole.DEFINER) ? "definers'" : "guessers'") + " turn.");
            System.out.println("Team \"" + currentTeam.getName() + "\"'s score: " + currentScore + " / " + currentTeam.getCardCount());
            System.out.println("Team \"" + currentTeam.getName() + "\"'s turn count: " + numberOfTurns);
            System.out.println("Next team's turn: Team \"" + nextTeam.getName() + "\"");
            printBoard(gameInstanceData, gameRole);
        }
    }

    public static void printBoard(@NotNull final GameInstanceData gameInstanceData, @NotNull final GameRole gameRole) {
        final GameStructure gameStructure = gameInstanceData.getGameStructure();
        final Board board = gameStructure.getBoard();
        final int boardRows = board.getRows();
        final int boardColumns = board.getColumns();
        final int totalCards = board.getCardCount() + board.getBlackCardCount();
        final List<Team> teams = gameStructure.getTeams();
        final List<WordCardData> wordCards = gameInstanceData.getWordCards();
        for (int i = 0; (i < boardRows) && (i*boardColumns < totalCards); i++) {
            List<WordCardData> currentLineList = wordCards.stream().skip((long) i * boardColumns).limit(boardColumns).collect(Collectors.toList());
            currentLineList.forEach(wordCard -> System.out.print(wordCard.getWord() + "  "));
            System.out.println();
            for (int j = 0; (j < boardColumns) && (i*boardColumns + j) < totalCards; j++) {
                final int currentIndex = i*boardColumns + j;
                System.out.print(createWordCardTag(wordCards.get(currentIndex), currentIndex, gameRole, teams) + "  ");
            }
            System.out.println();
        }
    }

    private static String createWordCardTag(final WordCardData wordCard, final int index, final GameRole gameRole,
                                            final List<Team> teams) {
        String wordCardTag = "[" + (index + 1) + "]";
        if (gameRole.equals(GameRole.DEFINER) || wordCard.isFound()) {
            if (gameRole.equals(GameRole.GUESSER)) {
                wordCardTag += " V";
            }
            else {
                wordCardTag += " " + (wordCard.isFound() ? "V" : "X");
            }
            if (teams.contains(wordCard.getTeam())) {
                wordCardTag += " (" + wordCard.getTeam().getName() + ")";
            }
            else if (wordCard.isBlackWord()){
                wordCardTag += " (BLACK)";
            }
        }
        return wordCardTag;
    }
}
