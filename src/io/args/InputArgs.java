package io.args;

import io.args.arg_type.DebugPictureType;
import io.args.arg_type.PictureSource;
import io.args.arg_type.PictureType;

public class InputArgs {


    @Override
    public String toString() {
        return "InputArgs{" +
                "k=" + k +
                ", sigma=" + sigma +
                ", pictureSource=" + pictureSource +
                ", pictureType=" + pictureType +
                ", path='" + path + '\'' +
                '}';
    }
    public int k;
    public double sigma;

    public PictureSource pictureSource;
    public PictureType pictureType;
    public String path;

    public byte command(String message) {
        if(message.length()<4){
            return -1;
        }else{
           String[] ms=message.substring(3).split(" ");
            if (ms.length!=2)return -1;
            switch (ms[0]){
                case "k":
                    try {
                        int sh=Short.parseShort(ms[1]);
                    } catch (NumberFormatException e){
                        return -1;
                    }
                    int sh=Short.parseShort(ms[1]);
                    if(sh<0||sh>10)return -1;
                    k=sh;
                    break;
                case "sigma":
                    try {
                        double sig=Short.parseShort(ms[1]);
                    } catch (NumberFormatException e){
                        return -1;
                    }
                    double sig=Short.parseShort(ms[1]);
                    if(sig<0||sig>10)return -1;
                    sigma=sig;
                    break;
                case "path":
                    break;
                case "source":
                    break;
                case "type":
                    break;
                default:
                    return -1;
            }
        }
        return 0;
    }
}
