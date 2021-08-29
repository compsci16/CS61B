package byog.Core;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o.getClass() == this.getClass())) {
            return false;
        }
        Position other = (Position) o;
        return other.x == this.x && other.y == this.y;
    }

    public static int xDistance(Position p1, Position p2) {
        return Math.abs(p1.x - p2.x);
    }

    public static int yDistance(Position p1, Position p2) {
        return Math.abs(p1.y - p2.y);
    }

    // assuming p1, p2 are corners of a rectangle
    public static int areaBound(Position p1, Position p2) {
        return (xDistance(p1, p2) - 1) * (yDistance(p1, p2) - 1);
    }

    public boolean isRightOf(Position p2) {
        return this.x > p2.x;
    }
}
