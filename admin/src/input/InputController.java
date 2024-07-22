package input;

import application.AdminApplication;
import application.GameAdder;
import exceptions.UnsupportedFileTypeException;
import lobby.LobbyController;
import org.jetbrains.annotations.NotNull;
import ui.UIElements;

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

    public static void mainMenuSelection() {
        int input = ClientInputController.intMenuInput(INTEGERS_FOR_MAIN_MENU);
        switch (input) {
            case 1:
                GameAdder.addNewGame();
                break;
            case 2:
                UIElements.printGameListAll();
                break;
            case 3:
                // TODO spectate game - after player-client work
                break;
            case 4:
                AdminApplication.tellToExit();
                break;
        }
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
