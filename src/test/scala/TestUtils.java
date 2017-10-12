import java.util.Random;

public class TestUtils {
    static char[][] initWithOnes(int size) {
        char[][] chars = new char[size][size];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) chars[i][j] = '1';

        return chars;
    }

    static void initWithRandomNumbers(Game game) {
        char[] ip = {'1', '1', '2', '4', '5', '6', '7', '8', '9'};
        Random random = new Random(0);
        int size = game.dimension;
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) game.board[i][j] = ip[random.nextInt(size - 1)];

        for (int i = 0; i < size; i++) {
            game.board[2][i] = '0';
            game.board[i][2] = '0';
        }
    }


}
