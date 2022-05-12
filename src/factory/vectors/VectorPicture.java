package factory.vectors;

import java.util.LinkedList;

public class VectorPicture {
    public boolean[][] edgedPixels;
    public Point[][] levelLinedpixels;
    public LinkedList<Region> regions;

    public short maxMagnitude;
    public short minMagnitude;


    public final int height;
    public final int width;

    public VectorPicture(int x, int y, boolean[][] edgedPixels){
        this.edgedPixels=edgedPixels;
        height=x;
        width=y;
    }
}
