package factory;

import factory.edges.EdgePicture;
import factory.vectors.VectorPicture;

import java.awt.image.BufferedImage;

public class Picture {
    public BufferedImage rawImage;
    public EdgePicture edgeImage;
    public VectorPicture vecImage;
    public byte fps;
    public boolean isNew;
    public boolean debugRendered;
}
