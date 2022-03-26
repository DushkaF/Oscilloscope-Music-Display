package factory.edges;

public class Edges {
    public short[][] greyPixels;
    public short[][] blurredPixels;
    public short[][] intensePixels;
    public short[][] angles;
    public short[][] suppressedPixels;
    public short[][] thresholdPixels;
    public boolean[][] edgedPixels;
    public final int height;
    public final int width;


    public Edges(int x, int y){
        blurredPixels= new short[x][y];
        intensePixels = new short[x][y];
        edgedPixels= new boolean[x][y];
        greyPixels= new short[x][y];
        angles= new short[x][y];
        suppressedPixels =new short[x][y];
        thresholdPixels =new short[x][y];
        this.height =x;
        this.width =y;
    }
}
