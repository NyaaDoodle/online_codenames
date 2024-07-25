package servlets;

import exceptions.GameListingException;
import exceptions.GameStructureFileException;
import lobby.LobbyManager;
import game.structure.GameStructure;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parsing.jaxb.JAXBConversion;
import utils.LogUtils;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.constants.Constants;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = Constants.NEW_GAME_SERVLET_NAME, urlPatterns = {Constants.NEW_GAME_RESOURCE_URI})
@MultipartConfig
public class NewGameServlet extends HttpServlet {
    private static final String STRUCTURE_FILE_PART_NAME = "structure-file";
    private static final String DICTIONARY_FILE_PART_NAME = "dictionary-file";
    private static final String JAXB_EXCEPTION_ERROR_PREFIX = "Invalid XML file according to schema-layout. Please correct the file to fit the schema requirements.\nAdditional info: ";

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        // TODO check if the user sending this is the administrator...
        // TODO AND IF THEY ARE LOGGED IN EVEN!!!
        try (InputStream structureStream = req.getPart(STRUCTURE_FILE_PART_NAME).getInputStream();
             InputStream dictionaryStream = req.getPart(DICTIONARY_FILE_PART_NAME).getInputStream()) {
            GameStructure gameStructure = JAXBConversion.parseToGameStructure(structureStream, dictionaryStream);
            LogUtils.logToConsole("Adding game \"" + gameStructure.getName() + "\" to game list.");
            addGameToLobby(gameStructure);
            ResponseUtils.sendPlainTextSuccess(res, "New game \"" + gameStructure.getName() + "\" was added successfully");
        } catch (final JAXBException e) {
            ResponseUtils.sendPlainTextBadRequest(res, JAXB_EXCEPTION_ERROR_PREFIX + e.getMessage());
        } catch (final GameStructureFileException e) {
            ResponseUtils.sendPlainTextBadRequest(res, e.getMessage());
        } catch (final GameListingException e) {
            ResponseUtils.sendPlainTextConflict(res, e.getMessage());
        }
    }

    private void addGameToLobby(final GameStructure gameStructure) throws GameListingException {
        LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
        synchronized (this) {
            if (!(lobbyManager.doesGameExist(gameStructure.getName()))) {
                lobbyManager.addGame(gameStructure);
            } else {
                throw new GameListingException("There already is a game with the same name: " + gameStructure.getName()
                        + "\nPlease provide a new name in the \"name\" value in the XML file.");
            }
        }
    }
}