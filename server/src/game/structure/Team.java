package game.structure;

import java.util.Objects;

public class Team {
    private final String name;
    private final int cardCount;
    private final int definersCount;
    private final int guessersCount;

    public Team(String name, int cardCount, int definersCount, int guessersCount) {
        this.name = name;
        this.cardCount = cardCount;
        this.definersCount = definersCount;
        this.guessersCount = guessersCount;
    }

    public String getName() {
        return name;
    }

    public int getCardCount() {
        return cardCount;
    }

    public int getDefinersCount() {
        return definersCount;
    }

    public int getGuessersCount() {
        return guessersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
