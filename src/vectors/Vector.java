package vectors;

import java.util.Objects;

public class Vector {
    private Point start;
    private Point end;
    private short length;

    public Vector(short xS, short yS,short xE, short yE){
        start=new Point(xS,yS);
        end=new Point(xS,yS);
        setLength();
    }

    public Vector(int xS, int yS, int xE, int yE){
        start=new Point(xS,yS);
        end=new Point(xS,yS);
        setLength();
    }
    public Vector(Point s, Point e){
        start=s;
        end=e;
        setLength();
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
        setLength();
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
        setLength();
    }

    public short getLength() {
        return length;
    }

    private short setLength(){
        return (short) Math.sqrt(Math.pow(start.getX()-end.getX(),2)+Math.pow(start.getY()-end.getY(),2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Objects.equals(start, vector.start) && Objects.equals(end, vector.end);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "start=" + start +
                ", end=" + end +
                ", length=" + length +
                '}';
    }
}
