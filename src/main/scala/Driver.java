import java.io.*;
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

                    Result result = new Result(cells, cellToChoose, game);
                    orderedSelection.add(result);
                    remaining = remaining - cells.size();
                }

            }
        }

        return orderedSelection;
    }


    int minimax(int depth, Game game, Player turn, int alpha, int beta, int max, int min) {
        PriorityQueue<Result> moves = tryAllActions(game);

        if (moves.isEmpty() || depth == 0)
            evaluate(max, min);

        else {
            while (!moves.isEmpty()) {
                if (turn.equals(Player.MAX)){

                    Result move = moves.poll();

                    Game child = move.game;
                    child.descendVisited();

                    int currentScore = minimax(depth - 1, child, Player.MIN, alpha, beta, max + child.score, min);
                    if (currentScore > alpha) {
                        alpha = currentScore;
                    }
                }
                else {
                    Result move = moves.poll();
                    Game child = move.game;
                    child.descendVisited();

                    int currentScore = minimax(depth - 1, child, Player.MAX, alpha, beta, max, min + child.score);
                    if (currentScore < beta) {
                        beta = currentScore;
                    }
                }

                if (alpha >= beta)
                    break;
            }
        }

        return (turn == Player.MAX) ? alpha : beta;
    }

    int evaluate(int max, int min) {
        return max - min;
    }

    public static void main(String[] args) throws IOException {
        Driver driver = new Driver();
        File src = new File(driver.getClass().getResource("input.txt").getFile());
        BufferedReader ip = new BufferedReader(new FileReader(src));

        int dim = Integer.parseInt(ip.readLine());
        ip.readLine();
        Game game = new Game(dim);

        for (int i = 0; i < dim; i++) {
            char[] fruit = ip.readLine().toCharArray();
            System.arraycopy(fruit, 0, game.board[i], 0, fruit.length);
        }

        ip.close();

        System.out.println(game);

        PriorityQueue<Result> results = driver.tryAllActions(game);
        System.out.println(results.size());
        while (!results.isEmpty())
            System.out.println(results.poll().game);
    }

}
