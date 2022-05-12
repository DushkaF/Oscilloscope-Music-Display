package factory.vectors;

import java.util.LinkedList;

public class Region {
    private LinkedList<Point> points;
    public Region(){
        points=new LinkedList<Point>();
    }
    public void addPoint(Point point){
        points.add(point);
    }
    public Point getPoint(int i){
        return points.size()>=i?null:points.get(i);
    }
    public Point pollPoint(){
        return points.poll();
    }
    public int size(){
        return points.size();
    }
}
