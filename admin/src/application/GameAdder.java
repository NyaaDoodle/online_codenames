package application;

import utils.http.HttpClientUtils;

import java.io.File;

public class GameAdder {
    public static void addNewGame() {
        File structureFile = null, dictionaryFile = null;
        newFilePromptMessage();
        boolean validInput = false;
        while (!validInput) {
            structureFile = loadStructureFile();
            dictionaryFile = loadDictionaryFile(structureFile);
            validInput = true;
        }
        HttpClientUtils.sendAddGameRequest(structureFile, dictionaryFile);
    }

    private static void newFilePromptMessage() {
        System.out.println("Write the full path of the game format file: (Supported file types: .xml)");
    }

    private static File loadStructureFile() {

    }

    private static File loadDictionaryFile(final File structureFile) {

    }


}
