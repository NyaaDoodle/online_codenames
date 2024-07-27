package utils;

import exceptions.JoinGameException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    private static final String PLAIN_TEXT = "text/plain";
    private static final String JSON_TYPE = "application/json";
    private static final String JOIN_GAME_ERROR_HEADER = "Error-Type";
    private static final String NO_PLAYER_STATUS_HEADER = "No-Player-Status";
    private static final String NO_PLAYER_STATUS_MESSAGE = "The requested game has ended and returned to pending, or you haven't signed up to a game yet.";

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

    public static void sendUnauthorized(final HttpServletResponse res) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public static void sendJoinGameError(final HttpServletResponse res, final String errorType, final String message, final int statusCode) throws IOException {
        res.setStatus(statusCode);
        res.setContentType(PLAIN_TEXT);
        res.addHeader(JOIN_GAME_ERROR_HEADER, errorType);
        res.getWriter().println(message);
    }

    public static void sendJoinGameError(final HttpServletResponse res, final JoinGameException e) throws IOException {
        sendJoinGameError(res, e.getErrorType(), e.getMessage(), e.getStatusCode());
    }

    public static void sendNoPlayerStatusError(final HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_CONFLICT);
        res.setContentType(PLAIN_TEXT);
        res.addHeader(NO_PLAYER_STATUS_HEADER, String.valueOf(true));
        res.getWriter().println(NO_PLAYER_STATUS_MESSAGE);
    }
}
