package factory.vectors;

import factory.Picture;
import factory.edges.EdgePicture;
import io.args.EditArgs;

import java.util.LinkedList;

import static java.lang.Math.*;

public class VecMain {
    private static int[][] gradientKernelX;
    private static int[][] gradientKernelY;
    private static int regMinSize;
    private static double tau;
    private static short numOfTries;

    public VectorPicture getFigures(Picture picture, EditArgs editArgs) {
        regMinSize=editArgs.regMinSize;
        tau=editArgs.tau;
        numOfTries=editArgs.numOfTries;
        EdgePicture edges=picture.edgeImage;
        VectorPicture vecPic=new VectorPicture(edges.height, edges.width, edges.edgedPixels);
        getLevelLines(vecPic);
        vecPic.regions=new LinkedList<>();
        getRegions(vecPic);
//        for (Region r: vecPic.regions) {
//            r.getRectangleAngle();
//            System.out.println(r);
//            System.out.println();
//        }

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
                points[i][j]=new Point(j,i,atan(1.0*Gy/Gx),(short) sqrt(Gx*Gx+Gy*Gy));
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
        vectorPicture.levelLinedpixels=points;

        return points;
    }

    private LinkedList<Region> getRegions(VectorPicture vecPic) {
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
                    growRegion(bins[i].poll(), region, vecPic, Sx, Sy);
                    if(region.size()>=regMinSize)regions.add(region);
                }
            }
        }
        regroupRegions(vecPic,regions);
      //  System.out.println(regions.size());
        for (int i = 0; i < regions.size(); i++) {
            Region r=regions.get(i);
            r.getRectangleAngle();
            if(!r.isDetected(vecPic.width, vecPic.height, tau)){
                regions.remove(i);
                i--;
            }else{
                r.createVector(vecPic.height, vecPic.width);
            }
        }
        //System.out.println(regions.size());
        vecPic.regions=regions;
        vecPic.vectors=new Vector[regions.size()];
        for (int i = 0; i < regions.size(); i++) {
            vecPic.vectors[i]=regions.get(i).vector;
        }
        return regions;
    }

    private void growRegion(Point point, Region region, VectorPicture vecPic, double sx, double sy) {
    //    System.out.println(point);
    //    System.out.println(region.regionAngle);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(point.x+j<0||point.x+j>= vecPic.width||point.y+i<0||point.y+i>=vecPic.height)){
                    if(!vecPic.levelLinedpixels[point.y+i][point.x+j].used){
                        if(abs(region.regionAngle-vecPic.levelLinedpixels[point.y+i][point.x+j].angle)<tau){
                            region.addPoint(vecPic.levelLinedpixels[point.y+i][point.x+j]);
                        //    System.out.println("added point");
                            vecPic.levelLinedpixels[point.y+i][point.x+j].used=true;
                            sx+=cos(vecPic.levelLinedpixels[point.y+i][point.x+j].angle);
                            sy+=sin(vecPic.levelLinedpixels[point.y+i][point.x+j].angle);
                            region.regionAngle=atan(sy/sx);
                            growRegion(vecPic.levelLinedpixels[point.y+i][point.x+j], region, vecPic, sx,sy);
                        }
                    }
                }
            }
        }
    }

    private void regroupRegions(VectorPicture vectorPicture, LinkedList<Region> regions){
        for (int numTry = 0; numTry < numOfTries; numTry++) {
            int h=vectorPicture.height/100+1;
            int w=vectorPicture.width/100+1;
            LinkedList<Region>[][] cells=new LinkedList[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    cells[i][j]=new LinkedList<>();
                }
            }
            int size=regions.size();
            for (int i = 0; i < size; i++) {
                    Region region=regions.poll();
                    region.used=false;
                    Point center=region.getCenter();
                    cells[center.y/100][center.x/100].add(region);
                    region.icellCoord=center.y/100;
                    region.jcellCoord=center.x/100;
            }
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    for (int k = 0; k < cells[i][j].size(); k++) {
                        Region region =cells[i][j].get(k);
                        if(!region.used){
                            region.used=true;
                            LinkedList<Region> newRegList=new LinkedList<>();
                            newRegList.add(region);
                            regrowRegion(cells, region, i, j, newRegList, region.regionAngle);
                            //length of new Region
                            /*int len=0;
                            for (int l = 0; l < newRegList.size(); l++) {
                                len+=newRegList.get(l).size();
                            }
                            System.out.print(len+" ");
                            //num of Regions
                            int len1=0;
                            for (int l = 0; l < h; l++) {
                                for (int m = 0; m < w; m++) {
                                    len1+=cells[i][j].size();
                                }
                            }
                            System.out.print(len1+" "+newRegList.size()+" ");*/
                            while (newRegList.size()!=1){
                                Region region1=newRegList.poll();
                                newRegList.getLast().addRegion(region1);
                                cells[region1.icellCoord][region1.jcellCoord].remove(region1);
                            }
                            /*len1=0;
                            for (int l = 0; l < h; l++) {
                                for (int m = 0; m < w; m++) {
                                    len1+=cells[i][j].size();
                                }
                            }
                            System.out.println(len1+" "+newRegList.peek().size());*/
                        }
                    }
                }
            }
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    for (int k = 0; k < cells[i][j].size(); k++) {
                        regions.add(cells[i][j].get(k));
                    }
                }
            }
        }
    }

    private void regrowRegion(LinkedList<Region>[][] cells, Region region, int icell, int jcell, LinkedList<Region> newRegion, double newAngle) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if(!(icell+i<0||icell+i>=cells.length||jcell+j<0||jcell+j>=cells[0].length)){
                    for (int k = 0; k < cells[icell+i][jcell+j].size(); k++) {
                        Region region1=cells[icell+i][jcell+j].get(k);
                        double distance = pow((region1.getCenter().x-region.getCenter().x),2)+pow((region1.getCenter().x-region.getCenter().x),2);
                        if(!region1.used&&abs(region1.regionAngle-newAngle)<=tau&&distance<=100){
                            newAngle=(newAngle*newRegion.size()+region1.regionAngle)/(newRegion.size()+1);
                            newRegion.add(region1);
                            region1.used=true;
                            regrowRegion(cells, region1, icell+i, jcell+j, newRegion,newAngle);
                        }
                    }
                }
            }
        }
    }
}
