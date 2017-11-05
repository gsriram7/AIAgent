import java.util.*;

class Agent {

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


    Selection minimax(int depth, Game game, Player turn, int alpha, int beta, int max, int min, Cell cell, long timeLeft, long startTime) {

        if ((System.currentTimeMillis() - startTime) > timeLeft) {
            System.out.println("timeout");
            if (turn.equals(Player.MAX))
                return new Selection(new Cell(-1, -1), Integer.MAX_VALUE);
            else
                return new Selection(new Cell(-1, -1), Integer.MIN_VALUE);
        }

        PriorityQueue<Result> moves = tryAllActions(game);

        if (moves.isEmpty() || depth == 0) {
            int score = evaluate(max, min);
            return new Selection(cell, score);
        } else {
            while (!moves.isEmpty()) {
                Result move = moves.poll();

                if (turn.equals(Player.MAX)) {

                    Game child = move.game;
                    child.descendVisited();

                    Selection selection = minimax(depth - 1, child, Player.MIN, alpha, beta, max + changeInScore(child.score, game.score), min, cell, timeLeft, startTime);
                    int currentScore = selection.score;
                    if (currentScore > alpha) {
                        cell = move.chosenCell;
                        alpha = currentScore;
                    }
                } else {
                    Game child = move.game;
                    child.descendVisited();

                    Selection selection = minimax(depth - 1, child, Player.MAX, alpha, beta, max, min + changeInScore(child.score, game.score), cell, timeLeft, startTime);
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

}
