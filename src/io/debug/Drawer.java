package io.debug;

import factory.Picture;
import factory.map.Cluster;
import factory.vectors.Region;
import factory.vectors.Vector;
import io.args.DebugArgs;
import io.args.arg_type.DebugPictureType;
import org.jsfml.graphics.*;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class Drawer {

    private static Vector2f firstPos;
    private static Vector2f secPos;
    private static Vector2f thirdPos;
    private static Vector2f fourthPos;

    private static Sprite firstSprite=new Sprite();
    private static Sprite secondSprite=new Sprite();
    private static Sprite thirdSprite=new Sprite();
    private static Sprite fourthSprite=new Sprite();

    private static Font font;
    private static Text text;
    private static Random random;
    public static void draw(Picture picture, RenderWindow window, DebugArgs dA, Vector2i size) throws TextureCreationException, IOException {
        firstPos=new Vector2f(0,0);
        secPos=new Vector2f(size.x/2, 0);
        thirdPos=new Vector2f(0,size.y/2);
        fourthPos=new Vector2f(size.x/2,size.y/2);
        Sprite sprite=new Sprite();

        if(!picture.debugRendered) {
            for (int i = 0; i < 4; i++) {
                Texture texture=new Texture();
                switch (dA.pictureList[i]) {
                    case RAW:
                        texture.loadFromImage(getImageFromRaw(picture));
                        break;
                    case REGIONS:
                        texture.loadFromImage(getImageFromRegions(picture));
                        break;
                    case VECTORS:
                        texture.loadFromImage(getImageFromVectors(picture));
                        break;
                    case CLUSTERS:
                        texture.loadFromImage(getImageFromClusters(picture));
                        break;
                    case ORDERED:
                        texture.loadFromImage(getImageFromOrdered(picture));
                        break;
                    default:
                        texture.loadFromImage(getImageFromEdges(picture, dA.pictureList[i]));
                        break;
                }
                sprite.setTexture(texture);
                switch (i) {
                    case 0:
                        sprite.setPosition(firstPos);
                        firstSprite.setPosition(firstPos);
                        firstSprite.setTexture(texture);
                        break;
                    case 1:
                        sprite.setPosition(secPos);
                        secondSprite.setPosition(secPos);
                        secondSprite.setTexture(texture);
                        break;
                    case 2:
                        sprite.setPosition(thirdPos);
                        thirdSprite.setPosition(thirdPos);
                        thirdSprite.setTexture(texture);
                        break;
                    case 3:
                        sprite.setPosition(fourthPos);
                        fourthSprite.setPosition(fourthPos);
                        fourthSprite.setTexture(texture);
                        break;
                }
                Vector2f scale = new Vector2f(1.0f * size.x / (2 * texture.getSize().x), 1.0f * size.y / (2 * texture.getSize().y));
                sprite.setScale(scale);
                firstSprite.setScale(scale);
                secondSprite.setScale(scale);
                thirdSprite.setScale(scale);
                fourthSprite.setScale(scale);
                window.draw(sprite);
            }
            picture.debugRendered=true;
        }else{
            window.draw(firstSprite);
            window.draw(secondSprite);
            window.draw(thirdSprite);
            window.draw(fourthSprite);
        }
        if(font==null){
            font=new Font();
            font.loadFromFile(Paths.get("src/io/debug/LucidaSansDemiBold.ttf"));
        }
        if(text==null){
            text= new Text();
            text.setFont(font);
            text.setColor(Color.RED);
            text.setCharacterSize(20);}
        text.setString(String.valueOf(picture.fps));
        window.draw(text);

    }

    private static Image getImageFromClusters(Picture picture) {
        Image image=new Image();
        image.create(picture.vecImage.width, picture.vecImage.height);
        LinkedList<Cluster> clusters=picture.vecImage.clusters;
        random=random==null?new Random():random;
        Vector vector;
        for (int  k= 0; k <clusters.size(); k++) {
            //  System.out.println(picture.vecImage.regions.get(k));
            // System.out.println(vector);
            Color color=new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
            for (int i = 0; i < clusters.get(k).size(); i++) {
                vector=clusters.get(k).get(i);
                if(vector.visible)drawVec(vector,color,image);
            }

        }
        return image;
    }

    private static Image getImageFromOrdered(Picture picture) {

        return getImageFromVectors(picture);
    }

    private static Image getImageFromVectors(Picture picture) {
        Image image=new Image();
        image.create(picture.vecImage.width, picture.vecImage.height);
        Vector[] vectors=picture.vecImage.vectors;
        random=random==null?new Random():random;
        Vector vector;
        for (int  k= 0; k < vectors.length; k++) {
            vector=vectors[k];
          //  System.out.println(picture.vecImage.regions.get(k));
           // System.out.println(vector);
            Color color=new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
            if(vector.visible)drawVec(vector,color,image);
        }
        return image;
    }
    private static void drawVec(Vector vector, Color color, Image image){
        int diffY=-(vector.start.y-vector.end.y);
        int diffX=-(vector.start.x-vector.end.x);
        double error=0.0;
        double step=abs(1.0*diffY/diffX);
        // System.out.println(diffX+" "+diffY);
        //System.out.println();
        if(diffX==0||diffY==0){
            if (diffX==0){
                //vertical
                if (diffY>0){
                    //down
                    for (int i = vector.start.y ; i < vector.end.y; i++) {
                        image.setPixel(vector.start.x,i,color);
                    }
                }else{
                    //up
                    for (int i = vector.start.y ; i > vector.end.y; i--) {
                        image.setPixel(vector.start.x,i,color);
                    }
                }
            }else{
                //horizontal
                if(diffX>0){
                    for (int i = vector.start.x ; i < vector.end.x; i++) {
                        image.setPixel(i,vector.start.y,color);
                    }
                    //right
                }else{
                    //left
                    for (int i = vector.start.x ; i > vector.end.x; i--) {
                        image.setPixel(i,vector.start.y,color);
                    }
                }
            }
        } else{if(diffX>0){
            //right
            if(diffY>0){
                //down
                if(diffY<diffX){
                    //from -45 to 0
                    int y=vector.start.y;
                    for (int i = vector.start.x; i < vector.end.x; i++) {
                        image.setPixel(i,y,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            y++;
                        }
                    }
                }else{
                    //from -90 to -45
                    int x=vector.start.x;
                    step=1/step;
                    for (int i = vector.start.y; i < vector.end.y; i++) {
                        image.setPixel(x,i,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            x++;
                        }
                    }
                }
            }else{
                //up
                if(abs(diffY)<diffX){
                    //from 0 to 45
                    int y=vector.start.y;
                    for (int i = vector.start.x; i < vector.end.x; i++) {
                        image.setPixel(i,y,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            y--;
                        }
                    }
                }else{
                    //from 45 to 90
                    int x=vector.start.x;
                    step=1/step;
                    for (int i = vector.start.y; i > vector.end.y; i--) {
                        image.setPixel(x,i,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            x++;
                        }
                    }
                }
            }
        }else{
            //left
            if(diffY>0){
                //down
                if(abs(diffX)>diffY){
                    //from -135 to -180
                    int y=vector.start.y;
                    for (int i = vector.start.x; i >vector.end.x; i--) {
                        image.setPixel(i,y,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            y++;
                        }
                    }
                }else{
                    //from -90 to -135
                    int x=vector.start.x;
                    step=1/step;
                    for (int i = vector.start.y; i < vector.end.y; i++) {
                        image.setPixel(x,i,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            x--;
                        }
                    }
                }
            }else{
                //up
                if(abs(diffX)>abs(diffY)){
                    //from 135 to 180
                    int y=vector.start.y;
                    for (int i = vector.start.x; i >vector.end.x; i--) {
                        image.setPixel(i,y,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            y--;
                        }
                    }
                }else{
                    //from 90 to 135
                    int x=vector.start.x;
                    step=1/step;
                    for (int i = vector.start.y; i > vector.end.y; i--) {
                        image.setPixel(x,i,color);
                        error+=step;
                        if(error>=1.0){
                            error--;
                            x--;
                        }
                    }
                }
            }
            }
        }

    }
    public static Image getImageFromRaw(Picture picture){
        Image image = new Image();
        image.create(picture.rawImage);
        return image;
    }
    private static Image getImageFromRegions(Picture picture){
        Image image = new Image();
        image.create(picture.vecImage.width, picture.vecImage.height);
        Region region;
        random=random==null?new Random():random;
        for (int i = 0; i < picture.vecImage.regions.size(); i++) {
                region=picture.vecImage.regions.get(i);
                Color color=new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
               // System.out.println(region);
                for (int j = 0; j < region.size(); j++) {
                //    System.out.println(j);
                //    System.out.println(region.getPoint(j));
                image.setPixel(region.getPoint(j).x, region.getPoint(j).y, color);
            }
        }
        return image;
    }
    private static Image getImageFromEdges(Picture picture, DebugPictureType debugPictureType){
        Image image = new Image();
        short matrix[][] =null;
        boolean edgeMatrix[][]=null;
        switch (debugPictureType) {
            case GREY:
                matrix = picture.edgeImage.greyPixels;
                break;
            case BLUR:
                matrix = picture.edgeImage.blurredPixels;
                break;
            case INTENSE:
                matrix = picture.edgeImage.intensePixels;
                break;
            case SUPPRESSED:
                matrix=picture.edgeImage.suppressedPixels;
                break;
            case THRESHOLD:
                matrix=picture.edgeImage.thresholdPixels;
            case EDGED:
                edgeMatrix = picture.edgeImage.edgedPixels;
                break;
        }
        if(matrix!=null){
            int x=matrix[0].length;
            int y=matrix.length;
            image.create(x,y);
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    image.setPixel(j,i,new Color(matrix[i][j],matrix[i][j],matrix[i][j]));
                   // System.out.print(matrix[i][j]+" ");
                }
               // System.out.println();
            }
        }else{
            int x=edgeMatrix[0].length;
            int y=edgeMatrix.length;
            image.create(x,y);
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    image.setPixel(j,i,edgeMatrix[i][j]?new Color(255,255,255):new Color(0,0,0));
                    // System.out.print(matrix[i][j]+" ");
                }
                // System.out.println();
            }
        }
        return image;
    }

}
