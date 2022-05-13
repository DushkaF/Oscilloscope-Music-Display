package factory.vectors;

import java.util.LinkedList;

public class Region {
    private LinkedList<Point> points;
    public double regionAngle;
    public long Sx;
    public long Sy;
    public boolean used;
    public int icellCoord;
    public int jcellCoord;
    public Region(){
        points=new LinkedList<Point>();
    }
    public void addPoint(Point point){
        points.add(point);
        Sx+=point.x;
        Sy+=point.y;
    }
    public Point getPoint(int i){
        return points.size()<=i?null:points.get(i);
    }
    public Point pollPoint(){
        Sx-=points.peek().x;
        Sy-=points.peek().y;
        return points.poll();
    }
    public Point peekPoint(){
        return points.peek();
    }
    public int size(){
        return points.size();
    }
    public void addRegion(Region region){
        for (int i = 0; i < region.size(); i++) {
            addPoint(region.peekPoint());
        }
        regionAngle=regionAngle/2*region.regionAngle/2;
    }
    public Point getCenter(){
        return new Point((int) (Sx/size()), (int) (Sy/size()),0,0);
    }
    @Override
    public String toString() {
        return "Region{" +
                "size="+ size()+
                ", points=" + points +
                ", regionAngle=" + regionAngle +
                '}';
    }
}
