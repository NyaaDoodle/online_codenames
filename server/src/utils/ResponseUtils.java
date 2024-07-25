package utils;

import exceptions.JoinGameException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    private static final String PLAIN_TEXT = "text/plain";
    private static final String JSON_TYPE = "application/json";
    public static void sendPlainTextConflict(final HttpServletResponse res, final String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType(PLAIN_TEXT);
        res.getWriter().println(errorMessage);
    }

    public static void sendPlainTextBadRequest(final HttpServletResponse res, final String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        res.setContentType(PLAIN_TEXT);
        res.getWriter().println(errorMessage);
    }

    public static void sendPlainTextSuccess(final HttpServletResponse res, final String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(PLAIN_TEXT);
        res.getWriter().println(message);
    }

    public static void sendJSONSuccess(final HttpServletResponse res, final String json) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(JSON_TYPE);
        res.getWriter().println(json);
    }

    public static void sendUnauthorized(final HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public static void sendJoinGameError(final HttpServletResponse res, final String errorType, final String message, final int statusCode) {
        
    }

    public static void sendJoinGameError(final HttpServletResponse res, final JoinGameException e) {
        sendJoinGameError(res, e.getErrorType(), e.getMessage(), e.getStatusCode());
    }
}
