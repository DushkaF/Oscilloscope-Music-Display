package factory.vectors;

public class Point {
    public int x;
    public int y;
    public double angle;
    public double magnitude;
    public boolean used;
    public Point(int x, int y, double angle, double magnitude) {
        this.x=x;
        this.y=y;
        this.magnitude= magnitude;
        this.angle=angle;
        used=false;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", magnitude=" + magnitude +
                '}';
    }
}
