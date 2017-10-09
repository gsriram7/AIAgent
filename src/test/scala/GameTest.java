import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class GameTest {

    Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(5);
        char[] ip = {'0', '1', '2', '4'};
        Random random = new Random(0);
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) game.board[i][j] = ip[random.nextInt(4)];

        for (int i = 0; i < 5; i++) {
            game.board[2][i] = '5';
            game.board[i][2] = '5';
        }
    }

    @Test
    public void shouldSayIfACellIsSafe() throws Exception {
        boolean isSafe = game.isSafe(game.board, '5', 2, 2);

        assertThat(isSafe, is(true));
    }

    @Test
    public void shouldSayNotSafeIfCellAlreadyVisited() throws Exception {
        game.board[2][2] = '*';

        boolean isSafe = game.isSafe(game.board, '5', 2, 2);

        assertThat(isSafe, is(false));
    }

    @Test
    public void shouldSayNotSafeIfADifferentCharPresent() throws Exception {
        game.board[2][2] = '4';

        boolean isSafe = game.isSafe(game.board, '5', 2, 2);

        assertThat(isSafe, is(false));
    }

    @Test
    public void shouldSayNotSafeIfCellIsOutOfBounds() throws Exception {

        assertThat(game.isSafe(game.board, '5', 2, 5), is(false));
        assertThat(game.isSafe(game.board, '5', 5, 2), is(false));
        assertThat(game.isSafe(game.board, '5', -1, 2), is(false));
        assertThat(game.isSafe(game.board, '5', 2, -1), is(false));
    }

    @Test
    public void shouldMarkConnectedComponents() throws Exception {
        HashSet<Cell> visited = game.dfs(2, 2);

        assertThat(visited.size(), is(9));

        for (Cell cell : visited) assertThat(game.board[cell.row][cell.col], is('*'));
    }
}