package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class World {
    private final Random RANDOM;
    private final TETile[][] world;
    private final int WIDTH;
    private final int HEIGHT;
    private final ArrayDeque<Room> rooms = new ArrayDeque<>();
    private final ArrayList<Position> roomWalls = new ArrayList<>();
    private final int RECURSION_DEPTH_LIMIT;

    public World(long seed, TETile[][] tiles, int width, int height) {
        this.RANDOM = new Random(seed);
        this.world = tiles;
        this.WIDTH = width;
        this.HEIGHT = height;
        RECURSION_DEPTH_LIMIT = 5 + RANDOM.nextInt(2);
    }

    public void render() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }

    public TETile[][] getWorld() {
        System.out.println(world);
        return world;
    }

    // fill with nothingness
    public void initialize() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        split();
        connectRooms();
        placeCorridorWalls();
        placeRandomLockedDoor();
    }

    private void split() {
        split(new Position(0, 0), new Position(WIDTH - 1, HEIGHT - 1), 0);
    }

    private void split(Position bottomLeft, Position topRight, int recursionDepth) {
        String direction = getRandomOrientation();
        // stop after 4 splitting iterations
        if (recursionDepth == RECURSION_DEPTH_LIMIT) {
            makeRandomRoom(bottomLeft, topRight);
            return;
        }
        recursionDepth++;
        if ("vertical".equals(direction)) { // splitVertically(bottomLeft, topRight);
            // calculate positions for splitting the left side
            // bottomLeft will remain same and the topRight's y will remain same but
            // the new X will be halved
            int midX = (bottomLeft.x() + topRight.x()) / 2;
            Position midTopRight = new Position(midX, topRight.y());
            split(bottomLeft, midTopRight, recursionDepth);

            // for right half:
            Position midBottomLeft = new Position(midX, bottomLeft.y());
            split(midBottomLeft, topRight, recursionDepth);
            // case horizontal
        } else { // splitHorizontally(bottomLeft, topRight);
            int midY = (bottomLeft.y() + topRight.y()) / 2;

            // for top half:
            Position midTopLeft = new Position(bottomLeft.x(), midY);
            split(midTopLeft, topRight, recursionDepth);

            // for bottom half:
            Position midTopRight = new Position(topRight.x(), midY);
            split(bottomLeft, midTopRight, recursionDepth);
        }
    }


    private void makeRandomRoom(Position bottomLeft, Position topRight) {
        if (Position.areaBound(bottomLeft, topRight) <= 0) {
            return;
        }
        // +1 to not collide with the walls
        int leftX = bottomLeft.x() + 1; // minimum
        int rightX = topRight.x() - 1; // maximum

        int bottomY = bottomLeft.y() + 1;
        int topY = topRight.y() - 1;

        if (rightX - leftX <= 1 || topY - bottomY <= 1) {
            return;
        }
        int x1 = leftX + RANDOM.nextInt(rightX - leftX + 1);
        int x2 = leftX + RANDOM.nextInt(rightX - leftX + 1);
        int roomLeftX = Math.min(x1, x2);
        int roomRightX = Math.max(x1, x2);


        int y1 = bottomY + RANDOM.nextInt(topY - bottomY + 1);
        int y2 = bottomY + RANDOM.nextInt(topY - bottomY + 1);
        int roomBottomY = Math.min(y1, y2);
        int roomTopY = Math.max(y1, y2);

        for (int i = roomLeftX; i <= roomRightX; i++) {
            for (int j = roomBottomY; j <= roomTopY; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        Room room = new Room(new Position(roomLeftX, roomBottomY),
                new Position(roomRightX, roomTopY));
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
                world[i][startY] = Tileset.FLOOR;
            }
            for (int i = startY; i <= endY; i++) {
                world[endX][i] = Tileset.FLOOR;
            }
        } else {
            for (int i = startX; i <= endX; i++) {
                world[i][startY] = Tileset.FLOOR;
            }
            for (int i = startY; i <= endY; i++) {
                world[endX][i] = Tileset.FLOOR;
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
            world[startX][i] = getColoredWall();
            roomWalls.add(new Position(startX, i));
        }

        // top horizontal wall
        for (int i = startX; i <= endX; i++) {
            world[i][endY] = getColoredWall();
            roomWalls.add(new Position(i, endY));
        }

        // right vertical wall
        for (int i = endY; i >= startY; i--) {
            world[endX][i] = getColoredWall();
            roomWalls.add(new Position(endX, i));
        }

        // bottom horizontal wall
        for (int i = endX; i >= startX; i--) {
            world[i][startY] = getColoredWall();
            roomWalls.add(new Position(i, startY));
        }
    }

    private void placeCorridorWalls() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                // if it's a FLOOR, then check if there is nothing at top and bottom
                // => horizontal corridor
                if (!world[i][j].equals(Tileset.FLOOR)) {
                    continue;
                }

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
                        world[p.x()][p.y()] = getColoredWall();
                    }
                }
            }
        }
    }

    private TETile getColoredWall() {
        return TETile.colorVariant(Tileset.WALL, 50, 50, 50, RANDOM);
    }

    private void placeRandomLockedDoor() {
        Position lockPos;
        do {
            int pos = RANDOM.nextInt(roomWalls.size());
            lockPos = roomWalls.get(pos);
        } while (!isAdjacentToFloorAndNothing(lockPos));
        world[lockPos.x()][lockPos.y()] = Tileset.LOCKED_DOOR;
    }

    private boolean isAdjacentToFloorAndNothing(Position p) {
        int i = p.x();
        int j = p.y();
        int[] neighbourXs = {i, i, i + 1, i - 1};
        int[] neighbourYs = {j + 1, j - 1, j, j};
        int floorCount = 0;
        int nothingCount = 0;
        for (int k = 0; k < 4; k++) {
            Position current = new Position(neighbourXs[k], neighbourYs[k]);
            if (isInvalidPosition(current)) {
                continue;
            }
            if (world[current.x()][current.y()].equals(Tileset.FLOOR)) {
                floorCount++;
                continue;
            }
            if (world[current.x()][current.y()].equals(Tileset.NOTHING)) {
                nothingCount++;
            }
        }
        return floorCount == 1 && nothingCount == 1;
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
        if (direction == 0) {
            return "horizontal";
        }
        return "vertical";
    }


}
