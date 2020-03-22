import java.awt.Graphics2D;

public class Line { // a line made up from 2 points
    private Point point1;
    private Point point2;

    public Line(Point point1, Point point2){ // initializes the line
        this.point1 = point1;
        this.point2 = point2;
    }

    public void draw(Graphics2D g){ // draws the line
        g.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
    }
}