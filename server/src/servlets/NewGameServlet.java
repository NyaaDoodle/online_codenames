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
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "NewGameServlet", urlPatterns = {"/new-game"})
@MultipartConfig
public class NewGameServlet extends HttpServlet {
    private static final String STRUCTURE_FILE_PART_NAME = "structure-file";
    private static final String DICTIONARY_FILE_PART_NAME = "dictionary-file";
    private static final String JAXB_EXCEPTION_ERROR_PREFIX = "Invalid XML file according to schema-layout. Please correct the file to fit the schema requirements.\nAdditional info: ";

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try (InputStream structureStream = req.getPart(STRUCTURE_FILE_PART_NAME).getInputStream();
             InputStream dictionaryStream = req.getPart(DICTIONARY_FILE_PART_NAME).getInputStream()) {
            GameStructure gameStructure = JAXBConversion.parseToGameStructure(structureStream, dictionaryStream);
            addGameToLobby(gameStructure);
            sendPlainTextSuccess(res, gameStructure.getName());
        } catch (final JAXBException e) {
            sendPlainTextConflict(res, e.getMessage(), JAXB_EXCEPTION_ERROR_PREFIX);
        } catch (final GameStructureFileException e) {
            sendPlainTextConflict(res, e.getMessage());
        } catch (final GameListingException e) {
            sendPlainTextConflict(res, e.getMessage());
        }
    }

    private void addGameToLobby(final GameStructure gameStructure) throws GameListingException {
        LobbyManager lobbyManager = ServletUtils.getLobbyManager(getServletContext());
        synchronized (this) {
            if (!(lobbyManager.doesGameAlreadyExist(gameStructure.getName()))) {
                lobbyManager.addGame(gameStructure);
            } else {
                throw new GameListingException("There already is a game with the same name: " + gameStructure.getName()
                        + "\n Please provide a new name in the \"name\" value in the XML file.");
            }
        }
    }

    private void sendPlainTextSuccess(final HttpServletResponse res, final String gameName) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("text/plain");
        res.getWriter().println("New game \"" + gameName + "\" was added successfully");
    }

    private void sendPlainTextConflict(final HttpServletResponse res, final String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType("text/plain");
        res.getWriter().println(errorMessage);
    }

    private void sendPlainTextConflict(final HttpServletResponse res, final String errorMessage, final String prefix) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType("text/plain");
        res.getWriter().println(prefix + errorMessage);
    }
}