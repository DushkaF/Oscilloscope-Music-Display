package factory;

import factory.edges.Edges;
import factory.vectors.Figures;
import io.args.DebugArgs;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.awt.image.BufferedImage;

public class Picture {
    private static Vector2f rawPos;
    private static Vector2f edgPos;
    private static Vector2f figPos;
    private Vector2f rawScale;
    private Vector2f edgScale;
    private Vector2f figScale;
    public BufferedImage rawImage;
    public Edges edgeImage;
    public Figures figures;
    public void draw(RenderWindow window, DebugArgs dA) throws TextureCreationException {

    }
    public Image getImageFromRaw(){
        Image image = new Image();
        image.create(rawImage);
        return image;
    }
    public Image getImageFromFigures(){
        Image image = new Image();
        image.create(rawImage);
        return image;
    }
    public Image getImageFromEdges(){
        Image image = new Image();
        image.create(rawImage);
        return image;
    }

}
/*
        if(rawPos==null)rawPos=new Vector2f(0,0);
        if(edgPos==null)edgPos=new Vector2f(window.getSize().x/2, 0);
        if(figPos==null)figPos=new Vector2f(0,window.getSize().y/2);

        Texture textureRaw = new Texture();
        textureRaw.loadFromImage(getImageFromRaw());
        Sprite spriteRaw=new Sprite(textureRaw);
        spriteRaw.setPosition(rawPos);
        if(rawScale==null)rawScale=new Vector2f(1.0f*window.getSize().x/(2*textureRaw.getSize().x),1.0f*window.getSize().y/(2*textureRaw.getSize().y));
        spriteRaw.setScale(rawScale);

        Texture textureEdg = new Texture();
        textureEdg.loadFromImage(getImageFromEdges());
        Sprite spriteEdge=new Sprite(textureEdg);
        spriteEdge.setPosition(edgPos);
        if(edgScale==null)edgScale=new Vector2f(1.0f*window.getSize().x/(2*textureEdg.getSize().x),1.0f*window.getSize().y/(2*textureEdg.getSize().y));
        spriteEdge.setScale(edgScale);

        Texture textureFig = new Texture();
        textureFig.loadFromImage(getImageFromFigures());
        Sprite spriteFig=new Sprite(textureFig);
        spriteFig.setPosition(figPos);
        if(figScale==null)figScale=new Vector2f(1.0f*window.getSize().x/(2*textureFig.getSize().x),1.0f*window.getSize().y/(2*textureFig.getSize().y));
        spriteFig.setScale(figScale);

       // System.out.println(window.getSize()+" "+textureRaw.getSize()+" "+rawScale+" ");
        window.draw(spriteRaw);
        window.draw(spriteEdge);
        window.draw(spriteFig);*/