package byog.Core;

import byog.TileEngine.TETile;

public class WorldVisualTest {
    public static void main(String[] args) {
        TETile[][] tiles = new TETile[60][30];
        System.out.println(System.currentTimeMillis()/1000);
        World w = new World(System.currentTimeMillis(), tiles, 60, 30);
        w.render();
    }
}
