import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DriverTest {

    Driver driver;
    Game game;

    @Before
    public void setUp() throws Exception {
        driver = new Driver();
        game = new Game(5);
        TestUtils.initWithRandomNumbers(game);
    }

    @Test
    public void shouldTryAllActionsAndExploreAllCells() throws Exception {
        PriorityQueue<Result> results = driver.tryAllActions(game);
        int items = 0;

        for (Result res : results) {
            items += res.connectedCells.size();
        }

        assertThat(items, is(game.dimension * game.dimension));
    }

    @Test
    public void exploredCellsAtEachIterationShouldBeDisjoint() throws Exception {
        PriorityQueue<Result> results = driver.tryAllActions(game);
        HashSet<Cell> items = new HashSet<>(game.dimension * game.dimension);

        for (Result res : results) {
            HashSet<Cell> visited = res.connectedCells;
            items.retainAll(visited);
            assertThat(items.size(), is(0));
            items.addAll(visited);
        }
    }

    @Test
    public void shouldReturnTopScoreFirst() throws Exception {
        PriorityQueue<Result> results = driver.tryAllActions(game);

        assertThat(results.poll().score, is(9 * 9));
    }

    @Test
    public void shouldVerifyTryAllActionsForEachResult() throws Exception {
        PriorityQueue<Result> results = driver.tryAllActions(game);

        while (!results.isEmpty()) {
            Result res = results.poll();
            Game g = new Game(this.game);
            HashSet<Cell> explored = g.dfs(res.chosenCell.row, res.chosenCell.col);

            assertThat(explored, is(res.connectedCells));
            assertThat(g.score, is(res.score));
        }
    }

    @Test
    public void shouldReturnOnly2ValuesForACustomDimension() throws Exception {
        int size = 5;
        Game game = new Game(TestUtils.initWithOnes(size));
        game.board[2][2] = '0';

        PriorityQueue<Result> results = driver.tryAllActions(game);

        assertThat(results.size(), is(2));

        Result res1 = results.poll();
        int explored = size * size - 1;
        assertThat(res1.score, is(explored * explored));
        assertThat(res1.connectedCells.size(), is(explored));

        Result res2 = results.poll();
        assertThat(res2.score, is(1));
        assertThat(res2.connectedCells.size(), is(1));
        assertTrue(res2.connectedCells.contains(new Cell(2,2)));
    }
}