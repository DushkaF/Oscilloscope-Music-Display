package factory.vectors;

import java.util.LinkedList;

public class VectorPicture {
    public Point[][] levelLinedpixels;
    public LinkedList<Region> regions;

    public short maxMagnitude;
    public short minMagnitude;


    public final int height;
    public final int width;

    public VectorPicture(int x, int y, double[][][] intensePixels){
        height=x;
        width=y;
        levelLinedpixels=new Point[height][width];
        short max=0;
        short min=1000;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                levelLinedpixels[i][j]=new Point(j,i,intensePixels[i][j][1],intensePixels[i][j][0]);
                if (levelLinedpixels[i][j].magnitude>max)max= (short) levelLinedpixels[i][j].magnitude;
                if (levelLinedpixels[i][j].magnitude<min)min= (short) levelLinedpixels[i][j].magnitude;
            }
        }
        maxMagnitude=max;
        minMagnitude=min;
        double scale=255/max;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                levelLinedpixels[i][j].magnitude*=scale;
            }
        }
    }
}
