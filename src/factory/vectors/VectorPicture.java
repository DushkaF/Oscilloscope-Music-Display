package factory.vectors;

import factory.map.Cluster;

import java.util.LinkedList;

public class VectorPicture {
    public boolean[][] edgedPixels;
    public Point[][] levelLinedpixels;
    public LinkedList<Region> regions;
    public Vector[] vectors;
    public LinkedList<Cluster> clusters;
    public Vector[][] orderedClusters;
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
