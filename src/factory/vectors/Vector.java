package factory.vectors;

import static java.lang.Math.*;

public class Vector {
    public int Cx;
    public int Cy;
    public double angle;
    public double minL;
    public double maxL;
    public double width;
    public Point start;
    public Point end;
    public Vector(int picHeight, int picWidth, int Cx,int Cy, double angle,double minL, double maxL,double width){
        //vector=new Vector2f(Cx+minL*sin(angle), )
        // Vector
       // System.out.println(picHeight+" "+picWidth+" "+Cx+" "+Cy+" "+angle+" "+minL+" "+maxL+" "+width);
        start=new Point((int) (Cx+minL*cos(angle)), (int) (Cy-minL*sin(angle)),angle,0);
        if(start.x<0)start.x=0;
        if(start.x>=picWidth)start.x=picWidth-1;
        if(start.y<0)start.y=0;
        if(start.y>=picHeight)start.y=picHeight-1;
        end=new Point((int) (Cx+maxL*cos(angle)), (int) (Cy-maxL*sin(angle)),angle,0);
        if(end.x<0)end.x=0;
        if(end.x>=picWidth)end.x=picWidth-1;
        if(end.y<0)end.y=0;
        if(end.y>=picHeight)end.y=picHeight-1;
        this.Cx=Cx;
        this.Cy=Cy;
        this.angle=angle;
        this.minL=minL;
        this.maxL=maxL;
        this.width=round(width);
     //   System.out.println(this);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "Cx=" + Cx +
                ", Cy=" + Cy +
                ", angle=" + angle +
                ", minL=" + minL +
                ", maxL=" + maxL +
                ", width=" + width +
                ", start={" + start.x+";"+start.y+"}" +
                ", end={" + end.x+";"+end.y+"}" +
                '}';
    }
}
