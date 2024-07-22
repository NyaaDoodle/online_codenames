package game.structure;

public class Board {
    private int cardCount;
    private int blackCardCount;
    private int rows;
    private int columns;

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

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public void setBlackCardCount(int blackCardCount) {
        this.blackCardCount = blackCardCount;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
