package exceptions;

public class GameStructureFileException extends Exception {
    public GameStructureFileException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}