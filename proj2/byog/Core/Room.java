package byog.Core;

public class Room {
    private final Position bottomLeft;
    private final Position topRight;
    private final int width;
    private final int height;
    private Position center;

    public Room(Position bottomLeft, Position topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.width = Position.xDistance(bottomLeft, topRight);
        this.height = Position.yDistance(bottomLeft, topRight);
        int midX = (bottomLeft.x() + topRight.x()) / 2;
        int midY = (bottomLeft.y() + topRight.y()) / 2;
        center = new Position(midX, midY);
    }

    public Position getBottomLeft() {
        return bottomLeft;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return height;
    }

    public Position center() {
        return center;
    }

    public Position getTopRight() {
        return topRight;
    }
}
