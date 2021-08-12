package byog.Core;

import byog.TileEngine.TETile;

public class WorldVisualTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        final long N = 1000L * 1000 * 1000;
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        World w = new World(6441, tiles, WIDTH, HEIGHT);
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < 1 * 60 * N) {
            w = new World(System.nanoTime(), tiles, WIDTH, HEIGHT);
            w.initialize();
        }
        w.render();
    }
}
