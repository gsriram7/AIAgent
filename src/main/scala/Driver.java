import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

class Driver {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Agent agent = new Agent();
        File src = new File("/Users/selvaram/selva/AIAgent/src/main/resources/input.txt");
        BufferedReader ip = new BufferedReader(new FileReader(src));

        int dim = Integer.parseInt(ip.readLine());
        int fruits = Integer.parseInt(ip.readLine());
        double timeLeft = Double.parseDouble(ip.readLine());
        timeLeft = timeLeft * 1000;

        Game original = new Game(dim);
        ArrayList<Cell> fruitCells = new ArrayList<>();

        for (int i = 0; i < dim; i++) {
            char[] fruit = ip.readLine().toCharArray();
            for (int j = 0; j < fruit.length; j++) {
                if (fruit[j] == '*')
                    original.fruitsPopped++;
                else fruitCells.add(new Cell(i, j));

                original.board[i][j] = fruit[j];
            }
        }

        ip.close();

        Game game = new Game(original);

        if (timeLeft < 1000) {
            rand(fruitCells, game);
        }
        else {
            int time = (int) timeLeft - 1000;
            int size = original.remainingFruits();
            int depth = Calib.getDepth(size, time);
            long timeInLong = new Integer(time).longValue();
            Selection selection = agent.minimax(depth, game, Player.MAX,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0,
                    new Cell(-1, -1), timeInLong, startTime);

            if (selection.cellToChoose.equals(new Cell(-1, -1))) {
                PriorityQueue<Result> results = agent.tryAllActions(new Game(original));
                Result top = results.peek();
                Cell cell = top.chosenCell;

                game.dfs(cell.row, cell.col);
                game.descendVisited();

                writeOutput(new Selection(cell, 100).formattedOutput(), game.toString());
            }
            else {
                game.dfs(selection.cellToChoose.row, selection.cellToChoose.col);
                game.descendVisited();

                writeOutput(selection.formattedOutput(), game.toString());
            }
        }
    }

    private static void rand(ArrayList<Cell> fruitCells, Game game) throws IOException {
        Random cell = new Random(0);
        int choice = cell.nextInt(fruitCells.size());
        Cell c = fruitCells.get(choice);
        game.dfs(c.row, c.col);
        game.descendVisited();

        writeOutput(new Selection(new Cell(c.row, c.col), 100).formattedOutput(), game.toString());
    }

    private static void writeOutput(String position, String board) throws IOException {
        File file = new File("output.txt");
        BufferedWriter br = new BufferedWriter(new FileWriter(file));
        br.write(position + "\n" + board);
        System.out.println(position + "\n" + board);
        br.flush();
        br.close();
    }


}
