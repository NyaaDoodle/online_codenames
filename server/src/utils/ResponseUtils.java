package utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    private static final String PLAIN_TEXT = "text/plain";
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
}
