import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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


    Selection minimax(int depth, Game game, Player turn, int alpha, int beta, int max, int min, Cell cell) {
        PriorityQueue<Result> moves = tryAllActions(game);

        if (moves.isEmpty() || depth == 0) {
            int score = evaluate(max, min);
            return new Selection(cell, score);
        }

        else {
            while (!moves.isEmpty()) {
                if (turn.equals(Player.MAX)){

                    Result move = moves.poll();

                    Game child = move.game;
                    child.descendVisited();

                    Selection selection = minimax(depth - 1, child, Player.MIN, alpha, beta, max + changeInScore(child.score, game.score), min, cell);
                    int currentScore = selection.score;
                    if (currentScore > alpha) {
                        cell = move.chosenCell;
                        alpha = currentScore;
                    }
                }
                else {
                    Result move = moves.poll();
                    Game child = move.game;
                    child.descendVisited();

                    Selection selection = minimax(depth - 1, child, Player.MAX, alpha, beta, max, min + changeInScore(child.score, game.score), cell);
                    int currentScore = selection.score;
                    if (currentScore < beta) {
                        cell = move.chosenCell;
                        beta = currentScore;
                    }
                }

                if (alpha >= beta)
                    break;
            }
        }

        return (turn == Player.MAX) ? new Selection(cell, alpha) : new Selection(cell, beta);
    }

    int changeInScore(int newScore, int oldScore) {
        return newScore - oldScore;
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

        Selection selection = driver.minimax(19, game, Player.MAX, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0, new Cell(-1, -1));
        System.out.println(selection);
    }

}
