import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class GameTest {

    Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(5);
        TestUtils.initWithRandomNumbers(game);
    }

    @Test
    public void shouldSayIfACellIsSafe() throws Exception {
        boolean isSafe = game.isSafe(game.board, '0', 2, 2);

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

    @Test
    public void shouldUpdateScoreAsSquareOfANumber() throws Exception {
        assertThat(game.score, is(0));

        game.updateScore(5);

        assertThat(game.score, is(25));
    }

    @Test
    public void shouldUpdateScoreAfterDfs() throws Exception {
        game.dfs(2, 2);

        assertThat(game.score, is(9 * 9));
    }

    @Test
    public void shouldNotDfsIfCellAlreadyVisited() throws Exception {
        game.board[2][2] = '*';
        HashSet<Cell> visited = game.dfs(2, 2);

        assertThat(visited.size(), is(0));
        assertThat(game.score, is(0));
    }

    @Test
    public void shouldMoveStarsToOneEndOfArray() throws Exception {
        char[] chars = new char[game.dimension];
        for (int i = 0; i < game.dimension; i++) chars[i] = '2';

        assertThat(game.descend(chars, 0), is(chars));

        chars[2] = '*';
        chars[4] = '*';

        assertThat(game.descend(chars, 2), is(new char[]{'*', '*', '2', '2', '2'}));
    }

    @Test
    public void shouldDescendFruitsThatAreExplored() throws Exception {
        char[][] ones = TestUtils.initWithOnes(5);
        Game test = new Game(ones);

        for (int i = 0; i < test.dimension; i++) {
            test.board[2][i] = '*';
            test.board[4][i] = '*';
        }

        test.board[3][0] = '*';
        test.board[3][1] = '*';

        System.out.println(Arrays.deepToString(test.board));

        test.descendVisited();

        for (int i = 0; i < ones.length; i++) {
            ones[0][i] = '*';
            ones[1][i] = '*';
        }

        ones[2][0] = '*';
        ones[2][1] = '*';

//        System.out.println(Arrays.deepToString(ones));
        System.out.println(Arrays.deepToString(test.board));

        assertThat(test.board, is(ones));
    }

    @Test
    public void shouldDescendAfterDfs() throws Exception {
        char[][] copy = game.getCopy();
        System.out.println(Arrays.deepToString(copy));
        System.out.println(Arrays.deepToString(game.board));
        System.out.println(Arrays.deepToString(game.board));
        game.dfs(2,2);

        System.out.println(Arrays.deepToString(game.board));
    }


    @Test
    public void shouldKeepTrackOfNumFruitsPopped() throws Exception {
        game.dfs(2,2);

        assertThat(game.numFruitsPopped, is(9));
    }

    @Test
    public void shouldInitNumFruitsPoppedInStartup() throws Exception {
        game.board[0][0] = '*';
        game.board[4][4] = '*';

        assertThat(new Game(game).numFruitsPopped, is(2));
    }

    @Test
    public void numFruitsDroppedShouldBeSameEvenAfterDescend() throws Exception {
        game.dfs(2,2);

        game.descendVisited();

        int stars = 0;
        for (int i = 0; i < game.dimension; i++)
            for (int j = 0; j < game.dimension; j++)
                if (game.board[i][j] == '*')
                    stars++;

        assertThat(game.numFruitsPopped, is(stars));
    }
}