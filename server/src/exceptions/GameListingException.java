package exceptions;

public class GameListingException extends Exception {
    public GameListingException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
