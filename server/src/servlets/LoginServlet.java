package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.LogUtils;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;

import java.io.IOException;

@WebServlet(name = Constants.LOGIN_SERVLET_NAME, urlPatterns = {Constants.LOGIN_RESOURCE_URI})
public class LoginServlet extends HttpServlet {
    private static final String USERNAME_QUERY_NAME = "username";
    private static final String USERNAME_BLANK_ERROR = "Username cannot be blank or unspecified.";
    private static final String CLIENT_ALREADY_LOGGED_IN_MESSAGE = "Client has already logged in, no need for additional logins.";
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        // TODO check reserved names on the server, and not on the client...
        final String sessionUsername = SessionUtils.getUsername(req);
        final UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (sessionUsername == null) {
            String parameterUsername = req.getParameter(USERNAME_QUERY_NAME);
            if (parameterUsername != null && !(parameterUsername.isEmpty())) {
                parameterUsername = parameterUsername.trim();
                synchronized (this) {
                    if (!(userManager.doesUserExist(parameterUsername))) {
                        LogUtils.logToConsole("Adding user \"" + parameterUsername + "\" to users.");
                        userManager.addUser(parameterUsername);
                        SessionUtils.setUsername(req, parameterUsername);
                        res.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        ResponseUtils.sendPlainTextConflict(res, "The user \"" + parameterUsername + "\" already exists. Please enter a different username.");
                    }
                }
            } else {
                ResponseUtils.sendPlainTextBadRequest(res, USERNAME_BLANK_ERROR);
            }
        }
        else {
            ResponseUtils.sendPlainTextConflict(res, CLIENT_ALREADY_LOGGED_IN_MESSAGE);
        }
    }
}
