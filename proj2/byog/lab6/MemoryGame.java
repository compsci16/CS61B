package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private final int width;
    private final int height;
    private int round;
    private final Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!"};

    private String currentEncouragement;

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        gameOver = false;
        playerTurn = false;
        round = 1;
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder random = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(25);
            random.append(CHARACTERS[x]);
        }
        return random.toString();
    }

    private void setFrameTop() {
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
        StdDraw.textLeft(0, height - 1, "Round: " + round);
        String middle = playerTurn ? "Type!" : "Watch!";
        StdDraw.text(1.0 * width / 2, height - 1, middle);
        StdDraw.textRight(width, height - 1, currentEncouragement);
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen

        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(StdDraw.WHITE);

        if (!gameOver) setFrameTop();

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));

        StdDraw.text(1.0 * width / 2, 1.0 * height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            wait(500);
            drawFrame(String.valueOf(letters.charAt(i)));
            wait(1000);
        }
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        while (i < n) {
            while (StdDraw.hasNextKeyTyped() && i < n) {
                s.append(StdDraw.nextKeyTyped());
                ++i;
            }
        }
        return s.toString();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        //drawFrame("Start Game");
        while (true) {
            playerTurn = false;
            currentEncouragement = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            String random = generateRandomString(round);
            System.out.println(random);
            flashSequence(random);
            playerTurn = true;
            drawFrame("");
            String typed = solicitNCharsInput(round);
            if (!random.equals(typed)) {
                gameOver = true;
                break;
            }
            round++;
        }
        drawFrame("Game Over! You made it to round: " + round);

        // Testing:
        // 1. drawFrame("Ho ho oh");
        // 2. flashSequence("Hohoho");
        // drawFrame("Type");
        // String typed = solicitNCharsInput(5);
        // System.out.println(typed);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }
}
