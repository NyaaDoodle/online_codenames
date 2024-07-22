package application;

import exceptions.UnsupportedFileTypeException;
import input.InputController;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import utils.http.HttpClientUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameAdder {
    private static final Set<String> STRUCTURE_FILE_TYPES = new HashSet<>(Collections.singletonList(".xml"));
    private static final Set<String> DICTIONARY_FILE_TYPES = new HashSet<>(Collections.singletonList(".txt"));
    private static final String XML_DICTIONARY_NODE_NAME = "ECN-Dictionary-File";

    private static final String STRUCTURE_FILE_TYPE_UNSUPPORTED = "File specified is not an XML file or does not end with \".xml\". Please enter a valid XML game format file.";
    private static final String DICTIONARY_FILE_TYPE_UNSUPPORTED = "Dictionary file specified in the game format file is not a TXT file or does not end with \".txt\""
            + "\nPlease provide a TXT file in \"ECN-Dictionary-File\" in the XML file.";
    private static final String STRUCTURE_NOT_FILE_OR_NOT_EXIST = "Input specified is not a file or does not exist. Please enter a valid game format file with its full path.";
    private static final String DICTIONARY_NOT_FILE_OR_NOT_EXIST = "Dictionary file specified in the game format file does not exist in the system, or is not a file.";
    private static final String IO_EXCEPTION_ERROR = "XML file could not be opened. This shouldn't happen because the file was checked up until this point...";
    private static final String PARSER_EXCEPTION_ERROR = "INTERNAL: Couldn't create the XML parser to get the dictionary file...";
    private static final String SAX_EXCEPTION_ERROR = "INTERNAL: XML parsing error while trying to get the XML DOM tree...";
    private static final String DICTIONARY_FILE_NULL = "Dictionary file name was not found on the specified game format file. Please correct the XML file to fit the schema requirements.";

    public static void addNewGame() {
        File structureFile = null, dictionaryFile = null;
        boolean addGameSuccess = false;
        while (!addGameSuccess) {
            newFilePromptMessage();
            try {
                structureFile = loadStructureFile();
                dictionaryFile = loadDictionaryFile(structureFile);
                Request req = buildAddGameRequest(structureFile, dictionaryFile);
                HttpClientUtils.sendRequest(req);
                addGameSuccess = true;
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
        }
        gameAddedSuccessfullyMessage();
        // TODO list games, basically use main menu option 2.
    }

    private static void newFilePromptMessage() {
        System.out.println("Write the full path of the game format file: (Supported file types: .xml)");
    }

    private static void gameAddedSuccessfullyMessage() {
        System.out.println("New game has been added successfully!");
    }

    @NotNull
    private static File loadStructureFile() throws Exception {
        File inputFile = null;
        boolean validInput = false;
        while (!validInput) {
            try {
                inputFile = InputController.fileInput(STRUCTURE_FILE_TYPES);
                validInput = true;
            } catch (UnsupportedFileTypeException e) {
                throw new Exception(STRUCTURE_FILE_TYPE_UNSUPPORTED);
            } catch (FileNotFoundException e) {
                throw new Exception(STRUCTURE_NOT_FILE_OR_NOT_EXIST);
            }
        }
        return inputFile;
    }

    @NotNull
    private static File loadDictionaryFile(final File structureFile) throws Exception {
        String dictionaryFileName = getDictionaryFileName(structureFile);
        File inputFile = null;
        boolean validInput = false;
        while (!validInput) {
            try {
                inputFile = InputController.fileInput(DICTIONARY_FILE_TYPES, dictionaryFileName);
                validInput = true;
            } catch (UnsupportedFileTypeException e) {
                throw new Exception(DICTIONARY_FILE_TYPE_UNSUPPORTED);
            } catch (FileNotFoundException e) {
                throw new Exception(DICTIONARY_NOT_FILE_OR_NOT_EXIST);
            }
        }
        return inputFile;
    }

    @NotNull
    private static String getDictionaryFileName(final File structureFile) throws Exception {
        String fileName = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(structureFile);
            fileName = doc.getElementsByTagName(XML_DICTIONARY_NODE_NAME).item(0).getFirstChild().getNodeValue());
        } catch (IOException e) {
            throw new Exception(IO_EXCEPTION_ERROR);
        } catch (ParserConfigurationException e) {
            throw new Exception(PARSER_EXCEPTION_ERROR);
        } catch (SAXException e) {
            throw new Exception(SAX_EXCEPTION_ERROR);
        }
        if (fileName != null) { return fileName; }
        else { throw new Exception(DICTIONARY_FILE_NULL); }
    }

    private static Request buildAddGameRequest(final File structureFile, final File dictionaryFile) {

    }
}
