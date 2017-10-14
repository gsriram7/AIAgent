import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Game {

    char[][] board;
    int dimension;
    int score = 0;
    int fruitsPopped = 0;

    Game(int dimension) {
        this.dimension = dimension;
        board = new char[dimension][dimension];
    }

    Game(char[][] copy) {
        this(copy.length);
        for (int i = 0; i < copy.length; i++)
            System.arraycopy(copy[i], 0, board[i], 0, copy.length);

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) if (board[i][j] == '*') fruitsPopped++;
    }

    Game(Game copy) {
        this(copy.board);
        this.score = copy.score;
    }

    boolean isSafe(char[][] b, char target, int row, int col) {
        return (row >= 0) && (row < dimension) &&
                (col >= 0) && (col < dimension) &&
                (b[row][col] == target && b[row][col] != '*');
    }

    private void dfs(char[][] b, char target, int row, int col, HashSet<Cell> visited) {
        int[] r = {0, 0, 1, -1};
        int[] c = {1, -1, 0, 0};

        visited.add(new Cell(row, col));

        for (int i = 0; i < r.length; i++) {
            int nextRow = row + r[i];
            int nextCol = col + c[i];
            if (isSafe(b, target, nextRow, nextCol)) {
                board[nextRow][nextCol] = '*';
                dfs(b, target, nextRow, nextCol, visited);
            }
        }
    }

    void updateScore(int number) {
        score = score + number * number;
    }

    void descendVisited() {
        for (int col = 0; col < dimension; col++) {
            char[] currentCol = new char[dimension];
            int countOfStars = 0;
            for (int row = 0; row < dimension; row++) {
                if (board[row][col] == '*') countOfStars++;

                currentCol[row] = board[row][col];
            }

            if (countOfStars != 0) {
                char[] newCol = descend(currentCol, countOfStars);

                for (int row = 0; row < dimension; row++) board[row][col] = newCol[row];
            }
        }
    }

    char[] descend(char[] currentCol, int countOfStars) {

        int starIndex = 0;
        int fruitIndex = countOfStars;
        char[] descendedFruits = new char[currentCol.length];

        for (char c : currentCol) {
            if (c == '*')
                descendedFruits[starIndex++] = c;
            else
                descendedFruits[fruitIndex++] = c;
        }

        return descendedFruits;

    }

    HashSet<Cell> dfs(int row, int col) {
        HashSet<Cell> visited = new HashSet<>(dimension * dimension);

        if (board[row][col] == '*')
            return visited;

        dfs(board, board[row][col], row, col, visited);

        updateFruitsPopped(visited.size());
        updateScore(visited.size());

        return visited;
    }

    void updateFruitsPopped(int size) {
        fruitsPopped += size;
    }

    boolean isGameOver() {
        return fruitsPopped >= (dimension*dimension);
    }

    public static void main(String[] args) {
        char[] ip = {'0', '1', '2', '4'};
        Random random = new Random(0);
        Game game = new Game(5);
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) game.board[i][j] = ip[random.nextInt(4)];

        for (int i = 0; i < 5; i++) {
            game.board[2][i] = '5';
            game.board[i][2] = '5';
        }

        game.board[1][4] = '5';
        game.board[0][4] = '5';
        game.board[3][4] = '5';
        game.board[4][4] = '5';

        System.out.println(game);
        game.dfs(2, 0);
        System.out.println(game);
    }

    char[][] getCopy() {
        char[][] copy = new char[dimension][dimension];
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                copy[i][j] = board[i][i];
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (char[] b : board) {
            String s = Arrays.toString(b);
            str.append(s).append("\n");
        }
        return str.toString();
    }
}
