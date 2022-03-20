package factory.edges;

public class Edges {
    public int rawPixels[][];
    public int blurredPixels[][];
    public int gradientPixels[][];
    public int edgedPixels[][];

    public final int height;
    public final int width;

    public Edges(int x, int y){
        rawPixels=new int[x][y];
        blurredPixels=new int[x][y];
        gradientPixels=new int[x][y];
        edgedPixels=new int[x][y];
        this.height =x;
        this.width =y;
    }
}
