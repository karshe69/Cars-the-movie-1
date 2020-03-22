import java.util.Objects;

public class Point { // a point
    protected double x; // position along the x axis
    protected double y; // position along the y axis

    public Point(){ // initializes the point with default parameters
        x = 0;
        y = 0;
    }

    public Point(double x, double y){ // initializes the point with given parameters
        this.x = x;
        this.y = y;
    }

    public void update(double x, double y){ // updates both x and y of the point
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.getX(), getX()) == 0 &&
                Double.compare(point.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
