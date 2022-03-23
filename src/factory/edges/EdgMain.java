package factory.edges;

import factory.Picture;
import io.args.EditArgs;

import java.awt.*;
import java.awt.image.BufferedImage;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class EdgMain {
    private static double[][] kernel;
    private static double lastSigma;
    public Edges getEdges(Picture picture, EditArgs editArgs) {
        BufferedImage raw = picture.rawImage;
        Edges edges=new Edges(raw.getHeight(), raw.getWidth());
        getGrey(edges,raw);
        blur(edges, editArgs.sigma, editArgs.k);
        picture.edgeImage=edges;
        return edges;
    }

    private void getGrey(Edges ed, BufferedImage raw){
        // Делаем двойной цикл, чтобы обработать каждый пиксель
        for (int i = 0; i < ed.height; i++) {
            for (int j = 0; j < ed.width; j++) {
                // Получаем цвет текущего пикселя
                Color color = new Color(raw.getRGB(j, i));

                // Получаем каналы этого цвета
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                // Применяем стандартный алгоритм для получения черно-белого изображения
                int grey = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
                //  Cоздаем новый цвет
                ed.greyPixels[i][j]=grey;
            }
        }
    }

    private void blur(Edges ed, double sigma, int k) {
        if(kernel==null||kernel.length!=2*k+1||lastSigma!=sigma){
            kernel=new double[2*k+1][2*k+1];
            for (int i = 0; i < 2*k+1; i++) {
                for (int j = 0; j < 2*k+1; j++) {
                    kernel[i][j]=1.0/(2*3.1415*sigma*sigma)*exp(-1.0*(pow(i-k-1,2)+pow(j-k-1,2))/(2*sigma*sigma));
                  //  System.out.print(kernel[i][j]+" ");
                }
              //System.out.println();
            }
            lastSigma=sigma;
        }

        for (int i = 0; i < ed.height; i++) {
            for (int j = 0; j < ed.width; j++) {
                double hS=0;
                for(int i2=-k;i2<k+1;i2++){
                    for(int j2=-k;j2<k+1;j2++){
                        if(i2+i>0&&i2+i<ed.height &&j2+j>0&&j2+j<ed.width){
                            hS+=ed.greyPixels[i+i2][j+j2]*kernel[i2+k][j2+k];
                        }
                    }
                }
               // double hS=1.0/(2*3.1415*sigma*sigma)*exp(-1.0*(pow(i-k-1,2)+pow(j-k-1,2))/(2*sigma*sigma))

                //System.out.println(hS);
                ed.blurredPixels[i][j]= (int) (hS);

            }
        }
    }


}
