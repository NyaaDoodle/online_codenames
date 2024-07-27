package client.exceptions;

public class NoPlayerStatusAtServerException extends Exception {
    public NoPlayerStatusAtServerException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
