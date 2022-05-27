package factory.map;

import factory.vectors.Vector;

import java.util.LinkedList;
;

public class Cluster {
    public LinkedList<Vector> vectors;
    public int Sx=0;
    public int Sy=0;
    public Cluster(){
        vectors=new LinkedList<>();
    }
    public int size(){
        return vectors.size();
    }
    public void add(Vector vector){
        vectors.add(vector);
        Sx+=vector.Cx;
        Sy+=vector.Cy;
    }
    public Vector peek(){
        return vectors.peek();
    }
    public Vector get(int i){
        if(i<0||i>= vectors.size())return null;
        return vectors.get(i);
    }
    public Vector poll(){
        Vector v=vectors.poll();
        Sx-=v.Cx;
        Sy-=v.Cy;
        return v;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "vectors=" + vectors +
                ", Sx=" + Sx +
                ", Sy=" + Sy +
                '}';
    }
}
