class Selection {

    final Cell cellToChoose;
    final int score;

    Selection(Cell cell, int score) {
        cellToChoose = new Cell(cell);
        this.score = score;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "cellToChoose=" + cellToChoose +
                ", score=" + score +
                '}';
    }
}
