import java.util.HashSet;
import java.util.PriorityQueue;

public class Driver {

    PriorityQueue<Result> tryAllActions(Game original) {

        int dimension = original.dimension;
        HashSet<Cell> explored = new HashSet<Cell>(dimension * dimension);
        PriorityQueue<Result> orderedSelection = new PriorityQueue<Result>(dimension * dimension);

        int remaining = dimension * dimension;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                Cell cellToChoose = new Cell(row, col);

                if (!explored.contains(cellToChoose) && remaining != 0 && original.board[row][col] != '*') {
                    Game game = new Game(original);
                    HashSet<Cell> cells = game.dfs(row, col);
                    explored.addAll(cells);
                    Result result = new Result(cells, cellToChoose, game.score);
                    orderedSelection.add(result);
                    remaining = remaining - cells.size();
                }

            }
        }

        return orderedSelection;
    }


}
