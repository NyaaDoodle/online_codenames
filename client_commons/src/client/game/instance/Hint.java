package client.game.instance;

public class Hint {
    private final String hintWords;
    private final int number;

    public Hint(String hintWords, int number) {
        this.hintWords = hintWords;
        this.number = number;
    }

    public String getHintWords() {
        return hintWords;
    }

    public int getNumber() {
        return number;
    }
}
