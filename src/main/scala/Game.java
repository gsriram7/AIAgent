import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Game {

    char[][] board;
    int size;

    public Game(int size) {
        this.size = size;
        board = new char[size][size];
    }

    public Game(char[][] copy) {
        this(copy.length);
        for (int i = 0; i < copy.length; i++)
            System.arraycopy(copy[i], 0, board[i], 0, copy.length);
    }

    boolean isSafe(char[][] b, char target, int row, int col) {
        return (row >= 0) && (row < size) &&
                (col >= 0) && (col < size) &&
                (b[row][col] == target && b[row][col] != '*');
    }

    void dfs(char[][] b, char target, int row, int col, HashSet<Cell> visited)  {
        int[] r = {0, 0, 1, -1};
        int[] c = {1, -1, 0, 0};

        //What if the curr cell is not target?
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


    HashSet<Cell> dfs(int row, int col) {
        HashSet<Cell> visited = new HashSet<>(size*size);

        dfs(board, board[row][col], row, col, visited);

        for (Cell cell : visited) board[cell.row][cell.col] = '*';

        return visited;
    }

    char[][] getCopy() {
        char[][] copy = new char[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                copy[i][j] = board[i][i];
        return copy;
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
