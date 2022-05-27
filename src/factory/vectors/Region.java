package factory.vectors;

import java.util.LinkedList;

import static java.lang.Math.*;

public class Region {
    private LinkedList<Point> points;
    public double regionAngle;
    private long Sx;
    private long Sy;
    private long Smagnitude;
    private int Cx;
    private int Cy;
    private double rectangleAngle;
    public boolean used;
    public int icellCoord;
    public int jcellCoord;
    public Vector vector;
    public Region(){
        points=new LinkedList<Point>();
    }
    public void addPoint(Point point){
        points.add(point);
        Sx+=point.x*point.magnitude;
        Sy+=point.y*point.magnitude;
        Smagnitude+=point.magnitude;
    }
    public Point getPoint(int i){
        return points.size()<=i?null:points.get(i);
    }

    public Point pollPoint(){
        Sx-=points.peek().x*points.peek().magnitude;
        Sy-=points.peek().y*points.peek().magnitude;
        Smagnitude-=points.peek().magnitude;
        return points.poll();
    }
    public Point peekPoint(){
        return points.peek();
    }
    public int size(){
        return points.size();
    }
    public void addRegion(Region region){

        regionAngle=regionAngle/2*region.regionAngle/2;
    }
    public Point getCenter(){
        Cx=(int) (1.0*Sx/Smagnitude);
        Cy=(int) (1.0*Sy/Smagnitude);
        return new Point(Cx,Cy ,0,0);
    }
    public double getRectangleAngle(){
        getCenter();
        long Sx_diff=0;
        long Sy_diff=0;
        long Sxy_diff=0;

        for (int i = 0; i < points.size(); i++) {
            Point p=points.get(i);
            Sx_diff+=p.magnitude*pow(p.x-Cx,2);
            Sy_diff+=p.magnitude*pow(p.y-Cy,2);
            Sxy_diff+=p.magnitude*(p.x-Cx)*(p.y-Cy);
        }
        //System.out.println("center"+getCenter());
       // System.out.println("diff sx sy sxy"+Sx_diff+" "+Sy_diff+" "+Sxy_diff);
        double mxx=1.0*Sx_diff/Smagnitude;
        double myy=1.0*Sy_diff/Smagnitude;
        double mxy=1.0*Sxy_diff/Smagnitude;
        double lambda=min(((mxx+myy)+sqrt(pow(mxx+myy,2)+4*mxy*mxy))/2,((mxx+myy)-sqrt(pow(mxx+myy,2)+4*mxy*mxy))/2);
        double recTg=(mxx-lambda)/mxy;
        //double recTg=mxy/(mxx-lambda);
       // if(Sx_diff)
        double recAngle=Double.isNaN(atan(recTg))? PI/2:PI/2+atan(recTg);
        //System.out.println("deg "+ toDegrees(recAngle)+" rad "+recAngle+" rad reg "+regionAngle);
        if(regionAngle*recAngle<0)recAngle-=PI;
        if((int)(toDegrees(recAngle))>=175&&regionAngle<0.1)recAngle-=PI;
       // System.out.println("deg "+ toDegrees(recAngle)+" rad "+recAngle+" rad reg "+regionAngle);
       //System.out.println();
        rectangleAngle=recAngle;
        return recAngle;
    }
    public boolean isDetected(int N, int M, double tau){
        boolean detection=true;
        int k=0;
        int n=size();
        for (Point p :points) {
            if(abs(p.angle-rectangleAngle)<tau)k++;
        }
      //  System.out.println(size()+" "+k);
        double NFM=pow(N*M, 2.5);
        double p=tau/PI;
        double s=0.0;
        for (int i = k ; i < n; i++) {
            s+=pow(p,i)*pow(1-p,n-i)*bcd(n,i);
        }
        return detection;
    }
    double bcd(int n,int k) {
        if (k>n/2) k=n-k; // возьмем минимальное из k, n-k.. В силу симметричности C(n,k)=C(n,n-k)
        if (k==1)  return n;
        if (k==0)  return 1;
        double r=1;
        for (int i=1; i<=k;++i) {
            r*=n-k+i;
            r/=i;
        }
        return ceil(r-0.2); // округлим до ближайшего целого, отбросив дробную часть
        // комментарий изменен после обсуждений: такой способ использован,  чтобы расхождение с точным значением
        // было как можно позже. Где-то оно превышало 0.5 и простой round не годился
    }

    public void createVector(int height, int width) {
        //y=y0 +(x-x0)*tg(recAngle), x0=Cx, y0=Height-y0, Ax+By+C=0 ---> R(p_i)=abs(Ax_i+Bx_i+C)/sqrt(A^2+B^2),
        // <R>=Sum(R_i*G_i)/Sum(G_i),  <R> - width of rec;
        //perpendicular => k -> -1/k =>y=y0-(x-x0)/tg(recAngle), x0=Cx, y0=Height-y0, Ax+By+C=0 ---> L(p_i)=Ax_i+Bx_i+C/sqrt(A^2+B^2),
        // Max_1=Max(L>0), Max_2=Min(L<0), length of rec = Max_1+abs(Max_2);
        //y=y0+(x-x0)*tg ---> x*(tg)+y*(-1)+(y0-x0*tg)=0
        //y=y0-(x-x0)/tg ---> x*(-1/tg)+y*(-1)+(y0+x0/tg)=0
        double minL=1;
        double maxL=-1;
        double s=0.0;
        for (int i = 0; i < size(); i++) {
            Point p =points.get(i);
            double tg=tan(rectangleAngle);
            double A=tg;
            double B=-1.0;
            double C=(height-Cy)-Cx*tg;
            double Ap=-1/tg;
            double Bp=-1;
            double Cp=(height-Cy)+Cx/tg;
            double R=abs(A*p.x+B*(height-p.y)+C)/sqrt(A*A+B*B);
            double L=(Ap*p.x+Bp*(height-p.y)+Cp)/sqrt(Ap*Ap+Bp*Bp);
            s+=R*p.magnitude;
            if (L>maxL)maxL=L;
            if(L<minL)minL=L;
        }
        double average_R=s/Smagnitude;
        if (average_R<1)average_R=1.0;
        minL=round(minL);
        maxL=round(maxL);
        //System.out.println("center " +Cx+" "+Cy+" angle "+toDegrees(rectangleAngle)+" legnth "+maxL+" "+-minL+" width "+average_R);
        vector=new Vector(height, width,Cx,Cy,regionAngle,minL,maxL,average_R, true);
        //vector=new Vector(height, width,Cx,Cy,rectangleAngle,minL,maxL,average_R, true);
        //System.out.println(vector);
    }

    @Override
    public String toString() {
        return "Region{" +
                "regionAngle=" + regionAngle +
                ", Cx=" + Cx +
                ", Cy=" + Cy +
                ", rectangleAngle=" + rectangleAngle +
                '}';
    }
}
