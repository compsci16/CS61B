package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class UserInterface {
    private final int WIDTH;
    private final int HEIGHT;
    private boolean playing;
    private World w;
    private Queue<String> moves;
    private boolean playingWithKeyboard;

    public UserInterface(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public UserInterface(int width, int height, Queue<String> moves) {
        this(width, height);
        this.playingWithKeyboard = true;
        this.moves = moves;
    }

    public void playWithKeyboard() throws IOException, ClassNotFoundException {
        executeCommand(moves.poll()); // N or L or Q at the beginning of keyboard string.
    }

    public TETile[][] getWorld() {
        return w.getWorld();
    }

    public void start() throws IOException, ClassNotFoundException {
        displayInitScreen();
        executeCommand(solicitNCharsInput(1));
    }

    private void executeCommand(String command) throws IOException, ClassNotFoundException {
        System.out.println(command);
        switch (command.toUpperCase()) {
            case "N":
                long seed;
                if (playingWithKeyboard) {
                    seed = getSeedFromKeyboard();
                } else {
                    drawFrame("(N)ew Game\n(L)oad\n(Q)uit\n \nEnter Seed");
                    seed = getSeedOnScreen();
                }
                System.out.println(seed);
                loadNewGame(seed);
                playing = true;
                playGame();
                break;
            case "L":
                loadPrevious();
                playing = true;
                playGame();
                break;
            case "Q":
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void playGame() {
        int x = w.getPlayerPos().x();
        int y = w.getPlayerPos().y();
        if (playingWithKeyboard && moves.size() == 0) {
            return;
        }

        Position p;
        boolean hasWon = false;
        do {
            String move = getMove();
            switch (move) {
                // north
                case "W":
                    p = new Position(x, y + 1);
                    break;
                // south
                case "S":
                    p = new Position(x, y - 1);
                    break;
                // west
                case "D":
                    p = new Position(x + 1, y);
                    break;
                // east
                case "A":
                    p = new Position(x - 1, y);
                    break;
                // pressed :
                // command :Q saves and exits the game.
                default:
                    if (solicitNCharsInput(1).equalsIgnoreCase("Q")) {
                        saveCurrentState();
                        System.exit(0);
                    }
                    playGame();
                    return;
            }
            if (w.isLockedDoor(p)) {
                hasWon = true;
                break;
            }
        } while (!w.isFloor(p));
        if (hasWon) {
            w.openLockedDoor();
            if (!playingWithKeyboard) {
                w.render();
            }
            return;
        }
        w.placePlayerAtPosition(p);
        if (!playingWithKeyboard) {
            w.render();
        }
        playGame();
    }

    private void saveCurrentState() {
        try {
            FileOutputStream fileOutputStream
                    = new FileOutputStream("load.txt");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(w);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadPrevious() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream("load.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        w = (World) objectInputStream.readObject();
        objectInputStream.close();
    }


    private long getSeedFromKeyboard() {
        StringBuilder s = new StringBuilder();
        String current = moves.poll();
        while (moves.size() > 0 && !current.equals("S")) {
            s.append(current);
            current = moves.poll();
        }
        System.out.println("Keyboard Seed: " + s);
        return Long.parseLong(s.toString());
    }

    private long getSeedOnScreen() {
        StringBuilder s = new StringBuilder();
        boolean keepGoing = true;
        while (keepGoing) {
            while (StdDraw.hasNextKeyTyped()) {
                char typedKey = StdDraw.nextKeyTyped();
                if (typedKey == 's' || typedKey == 'S') {
                    keepGoing = false;
                    continue;
                }
                if (!(48 <= typedKey && typedKey <= 57)) {
                    throw new IllegalArgumentException();
                }
                s.append(typedKey);
                drawFrame("(N)ew Game\n(L)oad\n(Q)uit\n \nEnter Seed\n" + s);
            }
        }
        return Long.parseLong(s.toString());
    }


    private void loadNewGame(long seed) {
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        w = new World(seed, tiles, WIDTH, HEIGHT);
        w.initialize();
        if (!playingWithKeyboard) {
            w.render();
        }
    }


    public String solicitNCharsInput(int n) {

        StringBuilder s = new StringBuilder();
        int i = 0;
        while (i < n) {
            if (playing) {
                w.displayMousePosition();
            }
            while (StdDraw.hasNextKeyTyped() && i < n) {
                s.append(StdDraw.nextKeyTyped());
                ++i;
            }
        }
        return s.toString();
    }

    public String getMove() {
        if (playingWithKeyboard) {
            return moves.poll();
        }

        String move;
        Set<String> permittedKeys = new HashSet<>(Arrays.asList("W", "A", "S", "D", ":"));
        do {
            move = solicitNCharsInput(1).toUpperCase();
        } while (!permittedKeys.contains(move));
        return move;
    }

    public void displayInitScreen() {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        drawFrame("(N)ew Game\n(L)oad\n(Q)uit\n ");
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.YELLOW);
        String[] lines = s.split("\n");
        int index = 0;
        for (int i = lines.length / 2; index < lines.length; i--) {
            StdDraw.text(1.0 * WIDTH / 2, 1.0 * HEIGHT / 2 + i, lines[index]);
            index++;
        }
        StdDraw.show();
    }

}
