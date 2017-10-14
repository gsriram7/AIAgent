import java.util.HashSet;

public class Result implements Comparable<Result> {

    HashSet<Cell> connectedCells;
    Cell chosenCell;
    int score;

    public Result(HashSet<Cell> visited, Cell cell, int score) {
        connectedCells = new HashSet<Cell>(visited.size());
        connectedCells.addAll(visited);

        this.chosenCell = new Cell(cell);
        this.score = score;
    }

    @Override
    public int compareTo(Result o) {
        return new Integer(o.score).compareTo(score);
    }
}
