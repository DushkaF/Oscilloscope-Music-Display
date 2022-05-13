package io.input;

import factory.Picture;
import factory.edges.EdgePicture;
import factory.vectors.VectorPicture;
import io.args.InputArgs;

import javax.imageio.ImageIO;
import java.io.*;

public class Input {
    private InputArgs inputArgs;
    private File file ;
    private String lastPath;
    public Input(InputArgs inputArgs) {
        this.inputArgs=inputArgs;
        lastPath="";
    }

    public Picture getPicture(Picture picture) {
        switch (inputArgs.pictureSource){
            case FILE:

                if(file==null||!file.getPath().equals(inputArgs.path))
                    file = new File(inputArgs.path);
                // System.out.println(file.getPath()+" "+lastPath);
                switch (inputArgs.pictureType){
                    case RAW:
                        if(!file.getPath().equals(lastPath)) {
                            try {
                                picture.rawImage = ImageIO.read(file);
                                lastPath= file.getPath();
                                System.out.println("loaded raw image from file: "+file.getPath());
                                picture.isNew=true;
                            } catch (IOException e) {
                                picture.rawImage=null;
                                System.out.println("Couldn't load raw image from file:"+file.getPath());
                            }
                        }
                        break;
                    case EDGED:
                        if(!file.getPath().equals(lastPath)) {
                            try {
                                ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file));
                                picture.edgeImage=(EdgePicture) objectInputStream.readObject();
                                lastPath= file.getPath();
                                System.out.println("loaded edged image from file: "+file.getPath());
                                picture.isNew=true;
                            } catch (IOException | ClassNotFoundException e) {
                                picture.edgeImage=null;
                                System.out.println("Couldn't load edged image from file:"+file.getPath());
                            }
                        }
                        break;
                    case VECTORIZED:
                        if(!file.getPath().equals(lastPath)) {
                            try {
                                ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file));
                                picture.vecImage =(VectorPicture) objectInputStream.readObject();
                                lastPath= file.getPath();
                                System.out.println("loaded vectorized image from file: "+file.getPath());
                                picture.isNew=true;
                            } catch (IOException | ClassNotFoundException e) {
                                picture.vecImage =null;
                                System.out.println("Couldn't load vectorized image from file:"+file.getPath());
                            }
                        }
                        break;
                }
                break;
            case URL:
                System.out.println("Getting picture from url is not supported");
                break;
            case SCREEN:
                System.out.println("Getting picture from the screen is not supported");
                break;
        }
        return picture;
    }

    public InputArgs getArgs() {
        return inputArgs;
    }

}
