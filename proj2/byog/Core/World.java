package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class World implements Serializable {
    private final Random RANDOM;
    private final TETile[][] world;
    private final int WIDTH;
    private final int HEIGHT;
    private final ArrayDeque<Room> rooms = new ArrayDeque<>();
    private final ArrayList<Position> roomWalls = new ArrayList<>();
    private Position playerPos;
    private final TERenderer ter;
    private Position lockedDoorPos;
    private final boolean isTextualWorld;
    public World(long seed, TETile[][] tiles, int width, int height, boolean isTextualWorld) {
        this.RANDOM = new Random(seed);
        this.world = tiles;
        this.WIDTH = width;
        this.HEIGHT = height;
        ter = new TERenderer();
        int xOff = 0;
        int yOff = 1;
        this.isTextualWorld = isTextualWorld;
        if(!isTextualWorld)
        ter.initialize(WIDTH + xOff, HEIGHT + yOff);
    }

    public void render(){
        if(!isTextualWorld)
        ter.renderFrame(world);
    }


    public void displayMousePosition() {

        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        int gridX = (int) Math.floor(mouseX);
        int gridY = (int) Math.floor(mouseY);

        Position mouse = new Position(gridX, gridY);
        if (isInvalidPosition(mouse)) {
            return;
        }
        // if (world[mouse.x()][mouse.y()].description().equals(mouseState)) return;
        this.render();
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(0, HEIGHT - 1, world[gridX][gridY].description());
        StdDraw.show();
    }

    public TETile[][] getWorld() {
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
        placePlayerRandomly();
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public boolean isLockedDoor(Position p) {
        return world[p.x()][p.y()].equals(Tileset.LOCKED_DOOR);
    }

    private void split() {
        split(new Position(0, 0), new Position(WIDTH - 1, HEIGHT - 1), 0);
    }

    private void split(Position bottomLeft, Position topRight, int recursionDepth) {
        String direction = getRandomOrientation();
//        if (((topRight.y() - bottomLeft.y()) <= 5)
//                ||
//                ((topRight.x() - bottomLeft.x()) <= 5)
//                ||
//                Position.areaBound(bottomLeft, topRight) <= (WIDTH * HEIGHT) / (10)) {
//            makeRandomRoom(bottomLeft, topRight);
//            return;
//        }
        if (Position.areaBound(bottomLeft, topRight)
                <=
                (WIDTH * HEIGHT) / (7 + RANDOM.nextInt(5))) {
            makeRandomRoom(bottomLeft, topRight);
            return;
        }
        // stop after 4 splitting iterations
//        if (recursionDepth == RECURSION_DEPTH_LIMIT) {
//            makeRandomRoom(bottomLeft, topRight);
//            return;
//        }
        // recursionDepth++;
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
        int[] xs = getRandom1dCoordinatesInRange(leftX, rightX, leftX);
        int roomLeftX = xs[0];
        int roomRightX = xs[1];

        int[] ys = getRandom1dCoordinatesInRange(bottomY, topY, bottomY);
        int roomBottomY = ys[0];
        int roomTopY = ys[1];

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

    private int[] getRandom1dCoordinatesInRange(int start, int upper, int lower) {
        int x1 = getBounded(start, upper, lower);
        int x2;
        do {
            x2 = getBounded(start, upper, lower);
        } while (x2 == x1);
        int min = Math.min(x1, x2);
        int max = Math.max(x1, x2);
        return new int[]{min, max};
    }

    private int getBounded(int start, int upper, int lower) {
        return start + RANDOM.nextInt(upper - lower + 1);
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
            lockPos = getRandomRoomWall();
        } while (!isAdjacentToFloorAndNothing(lockPos));
        world[lockPos.x()][lockPos.y()] = Tileset.LOCKED_DOOR;
        lockedDoorPos = lockPos;
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

    private Position getRandomFloor() {
        Position wallPos = getRandomRoomWall();
        int x = wallPos.x();
        int y = wallPos.y();
        int direction = RANDOM.nextInt(4);
        // 0 - N, 1 - S, 2 - E, 3 - W
        Position p;
        switch (direction) {
            case 0:
                p = new Position(x, y + 1);
                if (isFloor(p)) {
                    return p;
                }
                break;
            case 1:
                p = new Position(x, y - 1);
                if (isFloor(p)) {
                    return p;
                }
                break;
            case 2:
                p = new Position(x + 1, y);
                if (isFloor(p)) {
                    return p;
                }
                break;
            default:
                p = new Position(x - 1, y);
                if (isFloor(p)) {
                    return p;
                }
                break;
        }
        return getRandomFloor();
    }

    private void placePlayerRandomly() {
        playerPos = getRandomFloor();
        world[playerPos.x()][playerPos.y()] = Tileset.PLAYER;
    }

    public void placePlayerAtPosition(Position p) {
        if (playerPos == null) {
            throw new RuntimeException("Player Does Not exist");
        }
        world[playerPos.x()][playerPos.y()] = Tileset.FLOOR;
        playerPos = p;
        world[p.x()][p.y()] = Tileset.PLAYER;
    }

    public boolean isFloor(Position p) {
        return !isInvalidPosition(p) && world[p.x()][p.y()].equals(Tileset.FLOOR);
    }

    private Position getRandomRoomWall() {
        int pos = RANDOM.nextInt(roomWalls.size());
        return roomWalls.get(pos);
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


    public void openLockedDoor() {
        world[lockedDoorPos.x()][lockedDoorPos.y()] = Tileset.UNLOCKED_DOOR;
    }
}
