package hibiscus3000.tetris.model.math;

public class Point {

    public int x;
    public int y;

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {

    }
}
