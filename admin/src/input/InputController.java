package input;

import application.AdminApplication;
import application.GameAdder;
import application.GameSpectator;
import client.input.ClientInputController;
import client.lobby.game.list.GameListingData;
import exceptions.UnsupportedFileTypeException;
import client.lobby.game.list.GameList;
import game.room.GameRoom;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;
import utils.OtherUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputController extends ClientInputController {
    private static final Scanner SCANNER = getScanner();
    private static final int OPTIONS_IN_MAIN_MENU = 4;
    private static final Set<Integer> INTEGERS_FOR_MAIN_MENU
            = IntStream.rangeClosed(1, OPTIONS_IN_MAIN_MENU).boxed().collect(Collectors.toSet());
    private static final int OPTIONS_IN_GAME_ROOM_MENU = 2;
    private static final Set<Integer> INTEGERS_FOR_GAME_ROOM_MENU
            = OtherUtils.makeSetFromOneToN(OPTIONS_IN_GAME_ROOM_MENU);

    public static void mainMenuSelection() {
        int input = intMenuInput(INTEGERS_FOR_MAIN_MENU);
        switch (input) {
            case 1:
                GameAdder.addNewGame();
                break;
            case 2:
                UIElements.printGameListAll();
                break;
            case 3:
                // TODO spectate game - after player-client work
                GameSpectator.selectGame();
                break;
            case 4:
                AdminApplication.tellToExit();
                break;
        }
    }

    public static void gameRoomMenuSelection(final GameRoom gameRoom) {
        int input = intMenuInput(INTEGERS_FOR_GAME_ROOM_MENU);
        switch (input) {
            case 1:
                gameRoom.updateGameData();
                if (gameRoom.getGameData().getGameListingData().isGameActive()) {
                    UIElements.printGameData(gameRoom.getGameData(), gameRoom.getPlayerState().getRole());
                }
                else {
                    System.out.println("The game has ended and has returned to pending status. Exiting game room.");
                    gameRoom.setGameEnded();
                }
                break;
            case 2:
                gameRoom.exitGame();
                break;
        }
    }

    public static GameListingData spectateGameSelection(final GameList gameList) {
        final Set<Integer> integersForGameSelection = OtherUtils.makeSetFromOneToN(gameList.getGameAmount());
        int input = intMenuInput(integersForGameSelection);
        return gameList.getGameList().get(input - 1);
    }

    @NotNull
    public static File fileInput(final Set<String> supportedFileTypes) throws FileNotFoundException, UnsupportedFileTypeException {
        String inputPath = SCANNER.nextLine();
        return fileInput(supportedFileTypes, inputPath);
    }

    @NotNull
    public static File fileInput(final Set<String> supportedFileTypes, final String inputPath) throws FileNotFoundException, UnsupportedFileTypeException {
        if (supportedFileTypes.stream().anyMatch(inputPath::endsWith)) {
            File inputFile = new File(inputPath);
            if (inputFile.exists() && inputFile.isFile()) {
                return inputFile;
            }
            else {
                throw new FileNotFoundException();
            }
        }
        else {
            throw new UnsupportedFileTypeException();
        }
    }
}
