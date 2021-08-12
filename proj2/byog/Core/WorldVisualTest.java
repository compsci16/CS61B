package byog.Core;

import byog.TileEngine.TETile;

public class WorldVisualTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        World w = new World(6441, tiles, WIDTH, HEIGHT);
        w.initialize();
//        for (int i = 1; i < 100000; i++) {
//            w = new World(i, tiles, WIDTH, HEIGHT);
//            w.initialize();
//        }
        w.render();
    }
}
