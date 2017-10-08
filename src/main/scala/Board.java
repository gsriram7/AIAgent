public class Board {

    char[][] board;
    int size;

    public Board(int size) {
        this.size = size;
        board = new char[size][size];
    }

    public Board(char[][] copy) {
        this(copy.length);
        for (int i = 0; i < copy.length; i++)
            System.arraycopy(copy[i], 0, board[i], 0, copy.length);
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
