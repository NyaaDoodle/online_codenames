package application;

import exceptions.UnsupportedFileTypeException;
import input.InputController;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ui.UIElements;
import utils.constants.Constants;
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

    private static final String STRUCTURE_FILE_PART_NAME = "structure-file";
    private static final String DICTIONARY_FILE_PART_NAME = "dictionary-file";

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
        File structureFile, dictionaryFile;
        boolean addGameSuccess = false;
        boolean goBack = false;
        while (!addGameSuccess && !goBack) {
            newFilePromptMessage();
            UIElements.goBackOptionMessage();
            try {
                structureFile = loadStructureFile();
                if (structureFile != null) {
                    dictionaryFile = loadDictionaryFile(structureFile);
                    Request req = buildAddGameRequest(structureFile, dictionaryFile);
                    HttpClientUtils.sendRequestSync(req);
                    addGameSuccess = true;
                }
                else {
                    goBack = true;
                }
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (addGameSuccess) {
            gameAddedSuccessfullyMessage();
        }
    }

    private static void newFilePromptMessage() {
        System.out.println("Write the full path of the game format file: (Supported file types: .xml)");
    }

    private static void gameAddedSuccessfullyMessage() {
        System.out.println("New game has been added successfully!");
    }

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
        String dictionaryFileName = structureFile.getParent() + getFileSystemSlash() + getDictionaryFileName(structureFile);
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
        String fileName;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(structureFile);
            Node node = doc.getElementsByTagName(XML_DICTIONARY_NODE_NAME).item(0);
            if (node != null) {
                fileName = node.getFirstChild().getNodeValue();
            }
            else {
                throw new Exception(DICTIONARY_FILE_NULL);
            }
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
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart(STRUCTURE_FILE_PART_NAME, structureFile.getName(), RequestBody.create(structureFile, MediaType.parse("text/xml")))
                .addFormDataPart(DICTIONARY_FILE_PART_NAME, dictionaryFile.getName(), RequestBody.create(dictionaryFile, MediaType.parse("text/plain")))
                .build();
        return new Request.Builder().url(Constants.BASE_URL + Constants.NEW_GAME_RESOURCE_URI).post(body).build();
    }

    private static char getFileSystemSlash() {
        return (System.getProperty("os.name").contains("Windows")) ? '\\' : '/';
    }
}
