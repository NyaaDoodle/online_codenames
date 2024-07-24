package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import utils.constants.Constants;

@WebServlet(name = Constants.GAME_SERVLET_NAME, urlPatterns = {Constants.GAME_RESOURCE_URI})
public class GameServlet extends HttpServlet {
}
