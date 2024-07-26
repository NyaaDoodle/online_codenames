package game.structure;

import utils.constants.Constants;

import java.util.Objects;

public class Team {
    private final String name;
    private final int cardCount;
    private final int definersCount;
    private final int guessersCount;

    public Team() {
        // Used for the NEUTRAL_TEAM as an "empty team".
        this(null, Constants.ERROR_NUM, Constants.ERROR_NUM, Constants.ERROR_NUM);
    }

    public Team(final String name, final int cardCount, final int definersCount, final int guessersCount) {
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
