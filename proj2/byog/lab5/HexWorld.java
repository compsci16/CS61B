package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private boolean[][] occupied;
    private static final long SEED = 28731299;
    private static final Random RANDOM = new Random(SEED);

    public HexWorld(TETile[][] world) {
        occupied = new boolean[world.length][world[0].length];
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(8);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.GRASS;
            case 3:
                return Tileset.SAND;
            case 4:
                return Tileset.WATER;
            case 5:
                return Tileset.MOUNTAIN;
            case 6:
                return Tileset.FLOOR;
            case 7:
                return Tileset.TREE;
            default:
                return Tileset.LOCKED_DOOR;
        }
    }

    public void addHexagon(TETile[][] world, Position p, int s, TETile tile) {
        int cols = numberOfHexCols(s);
        int x = p.x();
        int y = p.y();

        // check edge cases
        if (x < 0 || y < 0 || y >= HEIGHT || x >= WIDTH) {
            return;
        }
        // check fitting
        if (x + cols >= WIDTH || y - (2 * s - 1) < 0)
            // as tiles are filled from right row-wise to the bottom
        {
            return;
        }

        if (occupied[x + s][y]) {
            return;
        }

        addTop(world, p, s, tile, cols);
        addBottom(world, p, s, tile, cols);

        p = new Position(x - cols + (s - 1), y + s);
        addHexagon(world, p, s, randomTile());  // add to top left

        p = new Position(x + cols - (s - 1), y + s);
        addHexagon(world, p, s, randomTile()); // add to top right

        p = new Position(x - cols + (s - 1), y - s);
        addHexagon(world, p, s, randomTile());  // add to bottom left

        p = new Position(x + cols - s + 1, y - s);
        addHexagon(world, p, s, randomTile()); // add to bottom right

    }

    public void addHexagon2(TETile[][] world, Position p, int s, TETile tile) {
        int cols = numberOfHexCols(s);
        int x = p.x();
        int y = p.y();

        // check edge cases
        if (x < 0 || y < 0 || y >= HEIGHT || x >= WIDTH) {
            return;
        }
        // check fitting
        if (x + cols >= WIDTH || y - (2 * s - 1) < 0) // as tiles are filled from right row-wise to the bottom
        {
            return;
        }

        if (occupied[x + s][y]) {
            return;
        }

        addTop(world, p, s, tile, cols);
        addBottom(world, p, s, tile, cols);
    }

    private void addTop(TETile[][] world, Position p, int s, TETile tile, int columns) {
        int spaces = columns / 2 - s / 2;
        int x = p.x();
        int y = p.y();
        for (int i = 0; i < s; i++) {
            addTiles(world, x, y - i, Tileset.NOTHING, spaces);
            addTiles(world, x + spaces, y - i, tile, s + 2 * i);
            spaces -= 1;
        }
    }

    private void addBottom(TETile[][] world, Position p, int s, TETile tile, int cols) {
        int spaces = 0;
        int x = p.x();
        int y = p.y() - s;
        for (int i = 0; i < s; i++) {
            addTiles(world, x, y - i, Tileset.NOTHING, spaces);
            addTiles(world, x + spaces, y - i, tile, cols - spaces * 2);
            spaces += 1;
        }
    }

    /**
     * @param world         Tile world
     * @param x             starting x position of tile placement
     * @param y             starting y for tile placement
     * @param tile          the tile to place
     * @param numberOfTiles number of tiles to place in the row
     */
    private void addTiles(TETile[][] world, int x, int y, TETile tile, int numberOfTiles) {
        for (int j = 0; j < numberOfTiles; j++) {
            if (!occupied[x + j][y] && !tile.equals(Tileset.NOTHING)) {
                world[x + j][y] = tile;
                occupied[x + j][y] = true;
            }
        }
    }


    private int numberOfHexCols(int s) {
        return s + 2 * (s - 1);
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        HexWorld creator = new HexWorld(world);
        int s = 3;
        int x = (WIDTH) / 2 - s / 2 - s;
        int y = HEIGHT / 2 - 3 * s / 2;
        creator.addHexagon(world, new Position(x, y), s, Tileset.FLOWER);
        // draws the world to the screen
        ter.renderFrame(world);

    }
}
