package exceptions;

public class JoinGameException extends Exception {
    private final String errorType;
    private final int statusCode;

    public JoinGameException(final String errorType, final String message, final int statusCode) {
        super(message);
        this.errorType = errorType;
        this.statusCode = statusCode;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getErrorType() {
        return errorType;
    }

    public int getStatusCode() {
        return statusCode;
    }
}