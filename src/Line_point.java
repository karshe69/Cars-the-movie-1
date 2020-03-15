import java.awt.Graphics2D;

public class Line_point{
    private Point point1;
    private Point point2;

    public Line_point(){
        point1 = new Point();
        point2 = new Point();
    }

    public Line_point(int x1, int y1, int x2, int y2){
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
    }

    public Line_point(Point point1, Point point2){
        this.point1 = point1;
        this.point2 = point2;
    }

    public Point getPoint1() {
        return point1;
    }

    public void draw(Graphics2D g){
        g.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }
}