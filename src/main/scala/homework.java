import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class homework {
    public static void main(String[] args) throws IOException {
        Driver driver = new Driver();
        File src = new File(driver.getClass().getResource("input.txt").getFile());
        BufferedReader ip = new BufferedReader(new FileReader(src));

        int dim = Integer.parseInt(ip.readLine());
        int fruits = Integer.parseInt(ip.readLine());
        double timeLeft = Double.parseDouble(ip.readLine());

        Game game = new Game(dim);

        for (int i = 0; i < dim; i++) {
            char[] fruit = ip.readLine().toCharArray();
            System.arraycopy(fruit, 0, game.board[i], 0, fruit.length);
        }

        ip.close();

        Selection selection = driver.minimax(19, game, Player.MAX, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0, new Cell(-1, -1));
        game.dfs(selection.cellToChoose.row, selection.cellToChoose.col);
        game.descendVisited();

        System.out.println(selection.formattedOutput());
        System.out.println(game);
    }

}
