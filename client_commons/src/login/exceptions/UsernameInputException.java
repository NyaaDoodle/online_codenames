package login.exceptions;

public class UsernameInputException extends Exception {
    public UsernameInputException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}