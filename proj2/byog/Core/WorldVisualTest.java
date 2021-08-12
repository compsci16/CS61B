package byog.Core;

import byog.TileEngine.TETile;

import java.awt.*;

public class WorldVisualTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        World w = new World(System.currentTimeMillis()/1000, tiles, WIDTH, HEIGHT);
        w.render();
    }
}
