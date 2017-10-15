class Selection {

    final Cell cellToChoose;
    final int score;

    Selection(Cell cell, int score) {
        cellToChoose = new Cell(cell);
        this.score = score;
    }

    String formattedOutput() {
        int ascii = 65;
        char col = (char) (ascii + cellToChoose.col);
        int row = cellToChoose.row + 1;
        return "" + col + row;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "cellToChoose=" + cellToChoose +
                ", score=" + score +
                '}';
    }
}
