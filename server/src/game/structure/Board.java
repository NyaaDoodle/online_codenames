package game.structure;

public class Board {
    private final int cardCount;
    private final int blackCardCount;
    private final int rows;
    private final int columns;

    public Board(int cardCount, int blackCardCount, int rows, int columns) {
        this.cardCount = cardCount;
        this.blackCardCount = blackCardCount;
        this.rows = rows;
        this.columns = columns;
    }

    public int getCardCount() {
        return cardCount;
    }

    public int getBlackCardCount() {
        return blackCardCount;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
