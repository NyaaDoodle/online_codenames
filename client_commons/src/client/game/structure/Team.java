package client.game.structure;

import java.util.Objects;

public class Team {
    private String name;
    private int cardCount;
    private int definersCount;
    private int guessersCount;

    public String getName() { return name; }

    public int getCardCount() {
        return cardCount;
    }

    public int getDefinersCount() {
        return definersCount;
    }

    public int getGuessersCount() {
        return guessersCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public void setDefinersCount(int definersCount) {
        this.definersCount = definersCount;
    }

    public void setGuessersCount(int guessersCount) {
        this.guessersCount = guessersCount;
    }
}
