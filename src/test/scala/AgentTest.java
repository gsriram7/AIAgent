import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AgentTest {

    Agent agent;
    Game game;

    @Before
    public void setUp() throws Exception {
        agent = new Agent();
        game = new Game(5);
        TestUtils.initWithRandomNumbers(game);
    }

    @Test
    public void shouldTryAllActionsAndExploreAllCells() throws Exception {
        PriorityQueue<Result> results = agent.tryAllActions(game);
        int items = 0;

        for (Result res : results) {
            items += res.connectedCells.size();
        }

        assertThat(items, is(game.dimension * game.dimension));
    }

    @Test
    public void exploredCellsAtEachIterationShouldBeDisjoint() throws Exception {
        PriorityQueue<Result> results = agent.tryAllActions(game);
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
        PriorityQueue<Result> results = agent.tryAllActions(game);

        assertThat(results.poll().game.score, is(9 * 9));
    }

    @Test
    public void shouldVerifyTryAllActionsForEachResult() throws Exception {
        PriorityQueue<Result> results = agent.tryAllActions(game);

        while (!results.isEmpty()) {
            Result res = results.poll();
            Game g = new Game(this.game);
            HashSet<Cell> explored = g.dfs(res.chosenCell.row, res.chosenCell.col);

            assertThat(explored, is(res.connectedCells));
            assertThat(g.score, is(res.game.score));
        }
    }

    @Test
    public void shouldReturnOnly2ValuesForACustomDimension() throws Exception {
        int size = 5;
        Game game = new Game(TestUtils.initWithOnes(size));
        game.board[2][2] = '0';

        PriorityQueue<Result> results = agent.tryAllActions(game);

        assertThat(results.size(), is(2));

        Result res1 = results.poll();
        int explored = size * size - 1;
        assertThat(res1.game.score, is(explored * explored));
        assertThat(res1.connectedCells.size(), is(explored));

        Result res2 = results.poll();
        assertThat(res2.game.score, is(1));
        assertThat(res2.connectedCells.size(), is(1));
        assertThat(res2.connectedCells.contains(new Cell(2, 2)), is(true));
    }

    @Test
    public void shouldVerifyBeforeDescend() throws Exception {
        HashSet<Cell> dfs = game.dfs(2, 2);

        PriorityQueue<Result> results = agent.tryAllActions(game);

        for (Result res : results) {
            dfs.retainAll(res.connectedCells);
            res.connectedCells.retainAll(dfs);
            assertThat(dfs.isEmpty(), is(true));
            assertThat(res.connectedCells.isEmpty(), is(true));
        }
    }

    @Test
    public void shouldBeEmptyIfAllExplored() throws Exception {
        Game game = new Game(TestUtils.initWithOnes(5));

        game.dfs(0, 0);

        PriorityQueue<Result> results = agent.tryAllActions(game);

        assertThat(results.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEvaluationResultFor2Values() throws Exception {
        assertThat(agent.evaluate(5, 4), is(1));
    }

    @Test
    public void shouldReturnChangeInScoreFor2Scores() throws Exception {
        assertThat(agent.changeInScore(5, 4), is(1));
        assertThat(agent.changeInScore(3, 4), is(-1));
    }

    //    @Test
    String drive(char[][] board, int depth) {
        Game game = new Game(board);
        boolean maxTurn = true;
        int maxScore = 0;
        int minScore = 0;
        String res = "";
        long start = System.currentTimeMillis();
//        while (!game.isGameOver() || (System.currentTimeMillis() - start) > 100000) {
            Agent agent = new Agent();
            Selection selection = agent.minimax(depth, game, Player.MAX, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0, new Cell(-1, -1), 30000, 1000000);

            if (selection.cellToChoose.row == -1 || selection.cellToChoose.col == -1) {
                return "-1L";
            }

            HashSet<Cell> dfs = game.dfs(selection.cellToChoose.row, selection.cellToChoose.col);
            game.descendVisited();

            if (maxTurn) maxScore += (dfs.size() * dfs.size());
            else minScore += (dfs.size() * dfs.size());

            maxTurn = !maxTurn;
//        }
        if (maxScore >= minScore) res = res + 'W';
        else res = res + 'L';
        return (System.currentTimeMillis() - start) + res;
    }

    char[][] populateGameBoard(int size) {
        char[] fruits = "0123456789".toCharArray();

        int dimension = 1;

        for (int i = 1; i <= 26; i++) {
            if (size <= (i * i)) {
                dimension = i;
                break;
            }
        }

        int stars = dimension * dimension - size;

        char[][] board = new char[dimension][dimension];

        Random random = new Random(0);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = fruits[random.nextInt(fruits.length)];
            }
        }

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (stars == 0)
                    break;
                else {
                    board[i][j] = '*';
                    stars--;
                }
            }
        }

        return board;
    }

    private void flushResults(String[][] results, String blu) {
        File file = new File("/Users/selvaram/selva/AIAgent/src/test/resources/time" + blu + ".txt");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            for (int i = 1; i < results.length; i++) {
                if (results[i][1] != null) {
                    String res = Arrays.deepToString(results[i]);
                    br.write(res + "\n");
                    br.flush();
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void collectStats(int start, int end, int minDepth, int maxDepth, String[][] results, String grp) {

        for (int i = minDepth; i <= maxDepth; i++) {
            for (int j = start; j <= end; j++) {
                results[j][0] = "" + j;
                String drive = drive(populateGameBoard(j), i);
                results[j][i] = drive;
                flushResults(results, grp);
            }
        }
    }

    public void shouldRecordRunTimes() throws Exception {
        String[][] results = new String[700][11];
        String name = "650";

        name = "650(4)";
        collectStats(500, 500, 3, 3, results, name);
        collectStats(550, 550, 3, 3, results, name);
        collectStats(600, 600, 3, 3, results, name);
        collectStats(650, 650, 3, 3, results, name);
        name = "650(44)";
        collectStats(500, 500, 4, 4, results, name);
        collectStats(550, 550, 4, 4, results, name);
        collectStats(600, 600, 4, 4, results, name);
        collectStats(650, 650, 4, 4, results, name);
    }


}