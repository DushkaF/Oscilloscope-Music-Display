package io.debug;

import factory.Picture;
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


public class Drawer {

    private static Vector2f firstPos;
    private static Vector2f secPos;
    private static Vector2f thirdPos;
    private static Vector2f fourthPos;
    private static Font font;
    private static Text text;
    public static void draw(Picture picture, RenderWindow window, DebugArgs dA, Vector2i size) throws TextureCreationException, IOException {
        firstPos=new Vector2f(0,0);
        secPos=new Vector2f(size.x/2, 0);
        thirdPos=new Vector2f(0,size.y/2);
        fourthPos=new Vector2f(size.x/2,size.y/2);

        Texture texture=new Texture();
        Sprite sprite=new Sprite();

        for(int i=0;i<4;i++){
            switch (dA.pictureList[i]){
                case RAW:
                    texture.loadFromImage(getImageFromRaw(picture));
                    break;
                case FIGURES:
                    texture.loadFromImage(getImageFromFigures(picture));
                    break;
                default:
                    texture.loadFromImage(getImageFromEdges(picture, dA.pictureList[i]));
                    break;
            }
            sprite.setTexture(texture);
            switch (i){
                case 0:
                    sprite.setPosition(firstPos);

                    break;
                case 1:
                    sprite.setPosition(secPos);

                    break;
                case 2:
                    sprite.setPosition(thirdPos);

                    break;
                case 3:
                    sprite.setPosition(fourthPos);

                    break;
            }
            //Vector2f scale=new Vector2f(1.0f*window.getSize().x/(2*texture.getSize().x),1.0f*window.getSize().y/(2*texture.getSize().y));
            Vector2f scale =new Vector2f(1.0f*size.x/(2*texture.getSize().x),1.0f*size.y/(2*texture.getSize().y));

            sprite.setScale(scale);
           window.draw(sprite);
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
    public static Image getImageFromRaw(Picture picture){
        Image image = new Image();
        image.create(picture.rawImage);
        return image;
    }
    private static Image getImageFromFigures(Picture picture){
        Image image = new Image();
        image.create(picture.rawImage);
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
