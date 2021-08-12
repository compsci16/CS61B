package byog.Core;


import java.util.Random;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (!(o.getClass() == this.getClass()))
            return false;
        Position other = (Position) o;
        return other.x == this.x && other.y == this.y;
    }

    public static int xDistance(Position p1, Position p2) {
        return Math.abs(p1.x - p2.x);
    }

    public static int yDistance(Position p1, Position p2) {
        return Math.abs(p1.y - p2.y);
    }

    public static int areaBound(Position p1, Position p2) {
        return xDistance(p1, p2) * yDistance(p1, p2);
    }

    public boolean isRightOf(Position p2) {
        return this.x > p2.x;
    }
}
