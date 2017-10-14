import java.util.HashSet;

class Result implements Comparable<Result> {

    HashSet<Cell> connectedCells;
    Cell chosenCell;
    Game game;

    Result(HashSet<Cell> visited, Cell cell, Game game) {
        connectedCells = new HashSet<Cell>(visited.size());
        connectedCells.addAll(visited);

        this.chosenCell = new Cell(cell);
        this.game = game;
    }

    @Override
    public int compareTo(Result o) {
        return new Integer(o.game.score).compareTo(game.score);
    }
}
