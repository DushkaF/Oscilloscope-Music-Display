package factory.edges;

import factory.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EdgMain {
    public Edges getEdges(Picture picture) {
        BufferedImage raw = picture.rawImage;
        Edges edges=new Edges(raw.getHeight(), raw.getWidth());
        getGrey(edges,raw);
        blur(edges, 1.4, 5);

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
                ed.rawPixels[i][j]=new Color(grey, grey, grey).getRGB();
            }
        }
    }

    private void blur(Edges ed, double sigma, int k) {
        for (int i = 0; i < ed.height; i++) {
            for (int j = 0; j < ed.width; j++) {
                double hS=0;
                for(int i2=-k/2;i2<k/2+1;i2++){
                    for(int j2=-k/2;j2<k/2+1;j2++){
                        if(i2+i>0&&i2+i<ed.height &&j2+j>0&&j2+j<ed.width){
                            hS+=Math.exp(1.0d*-(i2*i2+j2*j2)/(2*k*k))*ed.rawPixels[i][j];
                        }
                    }
                }
                ed.blurredPixels[i][j]= (int) (hS*1.0/(2*3.1415*k*k));
            }
        }
    }


}
