package factory.edges;

import factory.Picture;
import io.args.EditArgs;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class EdgMain {
    private static final byte HORIZONTAL =0;
    private static final byte SEC_DIAG =1;
    private static final byte VERTICAL =2;
    private static final byte DIAG =3;

    private static double[][] blurKernel;
    private static double lastSigma;
    private static int[][] gradientKernelX;
    private static int[][] gradientKernelY;

    private static short maxIntentse;

    public EdgePicture getEdges(Picture picture, EditArgs editArgs) {
        maxIntentse=0;
        BufferedImage raw = picture.rawImage;
        EdgePicture edges=new EdgePicture(raw.getHeight(), raw.getWidth());
        getGrey(edges,raw);
        blur(edges, editArgs.sigma, editArgs.k);
        getGradient(edges);
        supress(edges);
        threshold(edges, editArgs.highThresholdRatio, editArgs.lowThresholdRatio);
        hysteresis(edges, editArgs.numOfHyst);
        picture.edgeImage=edges;
        
        return edges;
    }


    private void getGrey(EdgePicture edges, BufferedImage raw){
        // Делаем двойной цикл, чтобы обработать каждый пиксель
        for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
                // Получаем цвет текущего пикселя
                Color color = new Color(raw.getRGB(j, i));

                // Получаем каналы этого цвета
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                // Применяем стандартный алгоритм для получения черно-белого изображения
                short grey = (short) (red * 0.299 + green * 0.587 + blue * 0.114);
                //  Cоздаем новый цвет
                edges.greyPixels[i][j]=grey;
            }
        }
    }

    private void blur(EdgePicture edges, double sigma, int k) {
        if(blurKernel ==null|| blurKernel.length!=2*k+1||lastSigma!=sigma){
            blurKernel =new double[2*k+1][2*k+1];
            for (int i = 0; i < 2*k+1; i++) {
                for (int j = 0; j < 2*k+1; j++) {
                    blurKernel[i][j]=1.0/(2*3.1415*sigma*sigma)*exp(-1.0*(pow(i-k-1,2)+pow(j-k-1,2))/(2*sigma*sigma));
                  //  System.out.print(kernel[i][j]+" ");
                }
              //System.out.println();
            }
            lastSigma=sigma;
        }

        for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
                double hS=0;
                for(int i2=-k;i2<k+1;i2++){
                    for(int j2=-k;j2<k+1;j2++){
                        if(i2+i>0&&i2+i<edges.height &&j2+j>0&&j2+j<edges.width){
                            hS+=edges.greyPixels[i+i2][j+j2]* blurKernel[i2+k][j2+k];
                        }
                    }
                }
               // double hS=1.0/(2*3.1415*sigma*sigma)*exp(-1.0*(pow(i-k-1,2)+pow(j-k-1,2))/(2*sigma*sigma))

                //System.out.println(hS);
                edges.blurredPixels[i][j]= (short) (hS);

            }
        }
    }
    private void getGradient(EdgePicture edges){
        if(gradientKernelX ==null) {
            gradientKernelX = new int[][] {{-1,-2,-1},{0,0,0},{1,2,1}};
        }
        if(gradientKernelY ==null) {
            gradientKernelY = new int[][] {{-1,0,1},{-2,0,2},{-1,0,1}};
        }
        int k=1;
        double angle;
        for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
                int Gx=0;
                int Gy=0;
                for(int i2=-k;i2<k+1;i2++){
                    for(int j2=-k;j2<k+1;j2++){
                        if(i2+i>0&&i2+i<edges.height &&j2+j>0&&j2+j<edges.width){
                            Gx+=edges.blurredPixels[i+i2][j+j2]*gradientKernelX[i2+k][j2+k];
                            Gy+=edges.blurredPixels[i+i2][j+j2]*gradientKernelY[i2+k][j2+k];
                        }
                    }
                }
                angle=atan(1.0*Gy/Gx);
                // System.out.print(Math.toDegrees(angle)+"\t");
                if(angle<-3*PI/8)edges.directions[i][j]=HORIZONTAL;
                if(angle>-3*PI/8&&angle<-PI/8)edges.directions[i][j]=SEC_DIAG;
                if(angle>-PI/8&&angle<PI/8)edges.directions[i][j]= VERTICAL;
                if(angle>PI/8&&angle<3*PI/8)edges.directions[i][j]= DIAG;
                if(angle>3*PI/8&&angle<5*PI/8)edges.directions[i][j]= HORIZONTAL;
                if(angle>5*PI/8&&angle<7*PI/8)edges.directions[i][j]= SEC_DIAG;
                if(angle>7*PI/8&&angle<9*PI/8)edges.directions[i][j]=VERTICAL;
                if(angle>9*PI/8&&angle<11*PI/8)edges.directions[i][j]=DIAG;
                if(angle>11*PI/8)edges.directions[i][j]=HORIZONTAL;
                edges.intensePixels[i][j][0]= (short) sqrt(Gx*Gx+Gy*Gy);
                edges.intensePixels[i][j][1]=angle;
             //   System.out.print(toDegrees(angle)+"\t");
            }
            //System.out.println();
        }
       /* for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
                System.out.print(edges.intensePixels[i][j][0]+"\t");
            }
            System.out.println();
        }*/
    }


    private void supress(EdgePicture edges){
        for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
                double p;
                double r;
                switch (edges.directions[i][j]){
                    case VERTICAL:
                        p=((i+1)<edges.height)? edges.intensePixels[i+1][j][0]:0;
                        r=((i-1)>=0)? edges.intensePixels[i-1][j][0]:0;
                        edges.suppressedPixels[i][j]= (short) ((edges.intensePixels[i][j][0]>p&&edges.intensePixels[i][j][0]>r)?edges.intensePixels[i][j][0]:0);
                        break;
                    case HORIZONTAL:
                        p=((j+1)<edges.width)? edges.intensePixels[i][j+1][0]:0;
                        r=((j-1)>=0)? edges.intensePixels[i][j-1][0]:0;
                        edges.suppressedPixels[i][j]= (short) ((edges.intensePixels[i][j][0]>p&&edges.intensePixels[i][j][0]>r)?edges.intensePixels[i][j][0]:0);
                        break;
                    case DIAG:
                        p=((i+1)<edges.height&&(j+1)<edges.width)? edges.intensePixels[i+1][j+1][0]:0;
                        r=((i-1)>=0&&(j-1)>=0)? edges.intensePixels[i-1][j-1][0]:0;
                        edges.suppressedPixels[i][j]= (short) ((edges.intensePixels[i][j][0]>p&&edges.intensePixels[i][j][0]>r)?edges.intensePixels[i][j][0]:0);
                        break;
                    case SEC_DIAG:
                        p=((i+1)<edges.height&&(j-1)>=0)? edges.intensePixels[i+1][j-1][0]:0;
                        r=((i-1)>=0&&(j+1)<edges.width)? edges.intensePixels[i-1][j+1][0]:0;
                        edges.suppressedPixels[i][j]= (short) ((edges.intensePixels[i][j][0]>p&&edges.intensePixels[i][j][0]>r)?edges.intensePixels[i][j][0]:0);
                        break;
                }
                maxIntentse=edges.suppressedPixels[i][j]>maxIntentse?edges.suppressedPixels[i][j]:maxIntentse;
            }
        }
    }
    private void threshold(EdgePicture edges, double highRatio, double lowRatio){
        short highThreshold = (short) (highRatio*maxIntentse);
        short lowThreshold = (short) (lowRatio*maxIntentse);
        for (int i = 0; i < edges.height; i++) {
            for (int j = 0; j < edges.width; j++) {
               short value=edges.suppressedPixels[i][j];
               edges.thresholdPixels[i][j]= (short) (value>highThreshold?255:value<lowThreshold?0:128);
            }
        }
    }
    private void hysteresis(EdgePicture edges, byte numOfHyst) {
        boolean hasStrong=false;
        for (int num=0;num<numOfHyst;num++){
            for (int i = 0; i < edges.height; i++) {
                for (int j = 0; j < edges.width; j++) {
                    hasStrong=false;
                    for(int i2=-1;i2<2;i2++){
                        for(int j2=-1;j2<2;j2++){
                            if(i2+i>0&&i2+i<edges.height &&j2+j>0&&j2+j<edges.width){
                                if (edges.thresholdPixels[i+i2][j+j2]==255||edges.edgedPixels[i+i2][j+j2]){
                                    hasStrong=true;
                                    break;
                                }
                            }
                        }
                    }
                    if (hasStrong&&edges.thresholdPixels[i][j]>=128)edges.edgedPixels[i][j]=true;
                }
            }
        }
    }

}
