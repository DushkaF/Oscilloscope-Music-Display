package factory.vectors;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

public class Rectangle {
    public int Cx;
    public int Cy;
    public double angle;
    public int minL;
    public int maxL;
    public int width;
    public RectangleShape rectangleShape;
    public Vector2f vector;
    public Rectangle(int height, int Cx,int Cy, double angle,int minL, int maxL,int width){
        rectangleShape=new RectangleShape();
        Vector2f size=new Vector2f(width,maxL-minL);
        rectangleShape.setSize(size);
        Vector2f position=new Vector2f(Cx+minL, Cy-width/2);
        rectangleShape.setPosition(position);
        Vector2f center =new Vector2f(-minL,width/2);
        rectangleShape.setOrigin(center);
        rectangleShape.rotate((float) toDegrees(angle));
        //vector=new Vector2f(Cx+minL*sin(angle), )
       // Vector
        this.Cx=Cx;
        this.Cy=Cy;
        this.angle=angle;
        this.minL=minL;
        this.maxL=maxL;
        this.width=width;
    }

}
