package client.exceptions;

public class JoinGameException extends Exception {
    private final String errorType;

    public JoinGameException(final String errorType, final String message) {
        super(message);
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}