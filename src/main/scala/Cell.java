class Cell {
    int row;
    int col;

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    Cell(Cell cell) {
        this.row = cell.row;
        this.col = cell.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (row != cell.row) return false;
        return col == cell.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ')';
    }
}
