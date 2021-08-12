package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;

public class World {
    private final long SEED;
    private final Random RANDOM;
    private final TETile[][] world;
    private final int WIDTH;
    private final int HEIGHT;
    private boolean occupied[][];
    private ArrayDeque<Room> rooms = new ArrayDeque<>();

    public World(long SEED, TETile[][] tiles, int width, int height) {
        this.SEED = SEED;
        this.RANDOM = new Random(SEED);
        this.world = tiles;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.occupied = new boolean[width][height];
    }

    public void render() {
        initialize();
        split();
        connectRooms();
        placeCorridorWalls();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

    // fill with nothingness
    private void initialize() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void split() {
        split(new Position(0, 0), new Position(WIDTH - 1, HEIGHT - 1), 0);
    }

    private void split(Position bottomLeft, Position topRight, int recursionDepth) {
        String direction = getRandomOrientation();
        // stop after 4 splitting iterations

        if (recursionDepth == 4) {
            makeRandomRoom(bottomLeft, topRight);
            return;
        }
        recursionDepth++;
        switch (direction) {
            case "vertical": {
                // splitVertically(bottomLeft, topRight);
                // calculate positions for splitting the left side
                // bottomLeft will remain same and the topRight's y will remain same but
                // the new X will be halved
                int midX = (bottomLeft.x() + topRight.x()) / 2;
                Position midTopRight = new Position(midX, topRight.y());
                split(bottomLeft, midTopRight, recursionDepth);

                // for right half:
                Position midBottomLeft = new Position(midX, bottomLeft.y());
                split(midBottomLeft, topRight, recursionDepth);
                break;
            }
            // case horizontal
            default: {
                // splitHorizontally(bottomLeft, topRight);
                int midY = (bottomLeft.y() + topRight.y()) / 2;

                // for top half:
                Position midTopLeft = new Position(bottomLeft.x(), midY);
                split(midTopLeft, topRight, recursionDepth);

                // for bottom half:
                Position midTopRight = new Position(topRight.x(), midY);
                split(bottomLeft, midTopRight, recursionDepth);
            }

        }
    }


    private void makeRandomRoom(Position bottomLeft, Position topRight) {
        if (Position.areaBound(bottomLeft, topRight) <= 7)
            return;
        int width, height;
        do {
            //
            width = RANDOM.nextInt(Position.xDistance(bottomLeft, topRight) - 1); // -1 because of walls
            height = RANDOM.nextInt(Position.yDistance(bottomLeft, topRight) - 1); // -1 because of walls
        } while (width <= 0 || height <= 0);

        /* TODO - add randomness to room generation; rn we start at bottom left coordinate,
                  you should be able to start at a different reasonable coordinate
         */
        // +1 to not collide with the walls
        int x = bottomLeft.x() + 1;
        int y = bottomLeft.y() + 1;
        for (int i = x; i <= x + width; i++) {
            for (int j = y; j <= y + height; j++) {
                world[i][j] = Tileset.GRASS;
            }
        }

        Room room = new Room(new Position(x, y), new Position(x + width, y + height));
        placeWallsAroundRoom(room);
        // adjacent rooms will be added adjacently in the deque
        rooms.add(room);
    }

    // since adjacent rooms were added together, we may remove two rooms at once and connect them
    private void connectRooms() {
        Room current = rooms.poll();
        // rooms.add(rooms.getFirst());
        while (!rooms.isEmpty()) {
            Room next = rooms.peek();
            connectPositions(current.center(), next.center());
            current = rooms.poll();
        }

    }

    private void connectPositions(Position p1, Position p2) {
        Position bottomPoint = Math.min(p1.y(), p2.y()) == p1.y() ? p1 : p2;
        Position topPoint = bottomPoint == p1 ? p2 : p1;
        int startX = bottomPoint.x();
        int startY = bottomPoint.y();
        int endX = topPoint.x();
        int endY = topPoint.y();

        if (bottomPoint.isRightOf(topPoint)) {
            for (int i = startX; i >= endX; i--) {
                world[i][startY] = Tileset.GRASS;
            }
            for (int i = startY; i <= endY; i++) {
                world[endX][i] = Tileset.GRASS;
            }
        } else {
            for (int i = startX; i <= endX; i++) {
                world[i][startY] = Tileset.GRASS;
            }
            for (int i = startY; i <= endY; i++) {
                world[endX][i] = Tileset.GRASS;
            }
        }

    }

    private void placeWallsAroundRoom(Room r) {
        Position bottomLeft = r.getBottomLeft();
        Position topRight = r.getTopRight();
        int startX = bottomLeft.x() - 1;
        int startY = bottomLeft.y() - 1;
        int endX = topRight.x() + 1;
        int endY = topRight.y() + 1;
        // left vertical wall
        for (int i = startY; i <= endY; i++) {
            world[startX][i] = Tileset.WALL;
        }

        // top horizontal wall
        for (int i = startX; i <= endX; i++) {
            world[i][endY] = Tileset.WALL;
        }

        // right vertical wall
        for (int i = endY; i >= startY; i--) {
            world[endX][i] = Tileset.WALL;
        }

        // bottom horizontal wall
        for (int i = endX; i >= startX; i--) {
            world[i][startY] = Tileset.WALL;
        }
    }

    private void placeCorridorWalls() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                // if it's a grass, then check if there is nothing at top and bottom => horizontal corridor
                if (!world[i][j].equals(Tileset.GRASS))
                    continue;

                Position[] neighbouringPositions = new Position[8];
                int c = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (!(k == i && l == j)) {
                            neighbouringPositions[c++] = new Position(k, l);
                        }
                    }
                }
                for (Position p : neighbouringPositions) {
                    if (isNothingAtPosition(p)) {
                        world[p.x()][p.y()] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private boolean isNothingAtPosition(Position p) {
        return !isInvalidPosition(p) && world[p.x()][p.y()].equals(Tileset.NOTHING);
    }


    private boolean isInvalidPosition(Position p) {
        return !(p.x() >= 0 && p.y() >= 0 && p.x() < WIDTH && p.y() < HEIGHT);
    }

    private void splitVertically(Position bottomLeft, Position topRight) {
        int midX = (bottomLeft.x() + topRight.x()) / 2;
        for (int i = bottomLeft.y(); i <= topRight.y(); i++) {
            world[midX][i] = Tileset.WATER;
        }
    }

    private void splitHorizontally(Position bottomLeft, Position topRight) {
        int midY = (bottomLeft.y() + topRight.y()) / 2;
        for (int i = bottomLeft.x(); i <= topRight.x(); i++) {
            world[i][midY] = Tileset.WATER;
        }
    }

    private String getRandomOrientation() {
        int direction = RANDOM.nextInt(2);
        switch (direction) {
            case 0:
                return "horizontal";
            default:
                return "vertical";
        }
    }


}
