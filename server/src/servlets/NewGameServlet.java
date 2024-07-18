package servlets;

import exceptions.GameStructureFileException;
import game.listing.GameListing;
import game.structure.GameStructure;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parsing.jaxb.JAXBConversion;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "NewGameServlet", urlPatterns = {"/new-game"})
@MultipartConfig
public class NewGameServlet extends HttpServlet {
    private static final String STRUCTURE_FILE_PART_NAME = "structure-file";
    private static final String DICTIONARY_FILE_PART_NAME = "dictionary-file";
    private static final String JAXB_EXCEPTION_ERROR = "Invalid XML file according to schema-layout. Please correct the file to fit the schema requirements.\nAdditional info: ";
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try ( InputStream structureStream = req.getPart(STRUCTURE_FILE_PART_NAME).getInputStream();
              InputStream dictionaryStream = req.getPart(DICTIONARY_FILE_PART_NAME).getInputStream()) {
            GameStructure gameStructure = JAXBConversion.parseToGameStructure(structureStream, dictionaryStream);
            addGameToGameList(gameStructure);
            sendSuccess(res, gameStructure.getName()); // TODO use gameListing instead!
        }
        catch (final JAXBException e) {
            sendJAXBError(res, e.getMessage());
        }
        catch (final GameStructureFileException e) {
            sendGameStructureFileError(res, e.getMessage());
        }
    }

    private void addGameToGameList(final GameStructure gameStructure) {
        // TODO make game listing, then add it to the list of game listings
        GameListing gameListing = new GameListing(gameStructure);

    }

    private void sendSuccess(final HttpServletResponse res, final String gameName) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("text/plain");
        res.getWriter().println("New game \"" + gameName + "\" was added successfully");
    }

    private void sendJAXBError(final HttpServletResponse res, final String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType("text/plain");
        res.getWriter().println(JAXB_EXCEPTION_ERROR + errorMessage);
    }

    private void sendGameStructureFileError(final HttpServletResponse res, final String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType("text/plain");
        res.getWriter().println(errorMessage);
    }
}
