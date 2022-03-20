package io.input;

import factory.Picture;
import factory.edges.Edges;
import factory.vectors.Figures;
import io.args.InputArgs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Input implements Runnable{
    private InputArgs inputArgs;
    private Thread inputThread;
    private File file ;
    private BufferedImage source;
    private Picture picture;

    public Input(InputArgs inputArgs) {
        this.inputArgs=inputArgs;
    }

    public Picture getPicture() {
        picture=new Picture();
        switch (inputArgs.pictureSource){
            case FILE:
                switch (inputArgs.pictureType){
                    case RAW:
                        if(file==null || file.getPath()!=inputArgs.path){
                            file=new File(inputArgs.path);
                        try {
                            picture.rawImage = ImageIO.read(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        }
                        break;
                    case EDGED:
                        try {
                            ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file));
                            picture.edgeImage=(Edges) objectInputStream.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case VECTORIZED:
                        try {
                            ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file));
                            picture.figures =(Figures) objectInputStream.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
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

    public String input(String nextLine) {
        return null;
    }

    public InputArgs getArgs() {
        return inputArgs;
    }

    @Override
    public void run() {

    }
}
