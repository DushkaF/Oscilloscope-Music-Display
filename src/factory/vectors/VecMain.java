package factory.vectors;

import factory.Picture;
import factory.edges.EdgePicture;
import io.args.EditArgs;

import java.util.LinkedList;

import static java.lang.Math.*;

public class VecMain {
    private static int[][] gradientKernelX;
    private static int[][] gradientKernelY;

    public VectorPicture getFigures(Picture picture, EditArgs editArgs) {
        EdgePicture edges=picture.edgeImage;
        VectorPicture vecPic=new VectorPicture(edges.height, edges.width, edges.edgedPixels);
        vecPic.levelLinedpixels=getLevelLines(vecPic);
        vecPic.regions=new LinkedList<>();
        vecPic.regions=getRegions(vecPic, editArgs.tau);

        /*for (int i = 0; i < vecPic.height; i++) {
            for (int j = 0; j < vecPic.width; j++) {
                System.out.print(vecPic.levelLinedpixels[i][j].angle+"\t");
            }
            System.out.println();
        }
        Region region;
        for (int i = 0; i < vecPic.regions.size(); i++) {
           region= vecPic.regions.get(i);
            for (int j = 0; i < region.size(); j++) {
                System.out.println(region.getPoint(j));
            }
            System.out.println("new region");
        }*/
        picture.vecImage=vecPic;
        return vecPic;
    }

    private Point[][] getLevelLines(VectorPicture vectorPicture){
        if(gradientKernelX ==null) {
            gradientKernelX = new int[][] {{-1,-2,-1},{0,0,0},{1,2,1}};
        }
        if(gradientKernelY ==null) {
            gradientKernelY = new int[][] {{-1,0,1},{-2,0,2},{-1,0,1}};
        }
        int k=1;
        int height=vectorPicture.edgedPixels.length;
        int width=vectorPicture.edgedPixels[0].length;
        short max=0;
        short min=1000;
        Point points[][]=new Point[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int Gx=0;
                int Gy=0;
                for(int i2=-k;i2<k+1;i2++){
                    for(int j2=-k;j2<k+1;j2++){
                        if(i2+i>0&&i2+i<height &&j2+j>0&&j2+j<width){
                            Gx+=(vectorPicture.edgedPixels[i+i2][j+j2]?255:0)*gradientKernelX[i2+k][j2+k];
                            Gy+=(vectorPicture.edgedPixels[i+i2][j+j2]?255:0)*gradientKernelY[i2+k][j2+k];
                        }
                    }
                }
                points[i][j]=new Point(i,j,atan(1.0*Gy/Gx),(short) sqrt(Gx*Gx+Gy*Gy));
                if(points[i][j].magnitude>max)max= (short) points[i][j].magnitude;
                if(points[i][j].magnitude<min)min= (short)points[i][j].magnitude;
            }
        }

        double scale=255.0/max;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                points[i][j].magnitude*=scale;
                points[i][j].magnitude=round(points[i][j].magnitude);
            }
        }
        vectorPicture.maxMagnitude=max;
        vectorPicture.minMagnitude=min;
        return points;
    }

    private LinkedList<Region> getRegions(VectorPicture vecPic, double tau) {
        LinkedList<Region> regions=new LinkedList<>();
        LinkedList<Point> bins[]= new LinkedList[11];
        for (int i = 0; i < vecPic.height; i++) {
            for (int j = 0; j < vecPic.width; j++) {
                if(bins[(int) (vecPic.levelLinedpixels[i][j].magnitude/25)]==null) bins[(int) (vecPic.levelLinedpixels[i][j].magnitude/25)]=new LinkedList<>();
                bins[(int) (vecPic.levelLinedpixels[i][j].magnitude/25)].add(vecPic.levelLinedpixels[i][j]);
            }
        }
        for (int i = 0; i <11; i++) {
            if(bins[i]!=null){
                while (bins[i].size()!=0) {
                    // System.out.println(bins[i].peek());
                    Region region=new Region();
                    region.regionAngle=bins[i].peek().angle;
                    double Sx=cos(region.regionAngle);
                    double Sy=sin(region.regionAngle);
                    growRegion(bins[i].poll(), region, vecPic, Sx, Sy, tau);
                    if(region.size()>=vecPic.height* vecPic.width/10000)regions.add(region);
                }
            }
        }

        return regions;
    }

    private void growRegion(Point point, Region region, VectorPicture vecPic, double sx, double sy, double tau) {
    //    System.out.println(point);
    //    System.out.println(region.regionAngle);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (!(point.x+j<0||point.x+j>= vecPic.width||point.y+i<0||point.y+i>=vecPic.height)){
                    if(!vecPic.levelLinedpixels[point.y+i][point.x+j].used){
                        if(abs(region.regionAngle-vecPic.levelLinedpixels[point.y+i][point.x+j].angle)<tau){
                            region.addPoint(vecPic.levelLinedpixels[point.y+i][point.x+j]);
                        //    System.out.println("added point");
                            vecPic.levelLinedpixels[point.y+i][point.x+j].used=true;
                            sx+=cos(vecPic.levelLinedpixels[point.y+i][point.x+j].angle);
                            sy=sin(vecPic.levelLinedpixels[point.y+i][point.x+j].angle);
                            region.regionAngle=atan(sy/sx);
                            growRegion(vecPic.levelLinedpixels[point.y+i][point.x+j], region, vecPic, sx,sy,tau);
                        }
                    }
                }
            }
        }
    }
}
