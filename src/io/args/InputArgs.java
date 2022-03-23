package io.args;

import io.args.arg_type.PictureSource;
import io.args.arg_type.PictureType;

import java.util.Objects;

public class InputArgs {

    public PictureSource pictureSource;
    public PictureType pictureType;
    public String path;

    public InputArgs(){
        pictureSource=PictureSource.FILE;
        pictureType=PictureType.RAW;
        path="";
    }


    public byte command(String message) {
        if(message.length()<4){
            return -1;
        }else{
           String[] ms=message.substring(3).split(" ");
            if (ms.length!=2)return -1;
            switch (ms[0]){
                case "path":
                    path=ms[1];
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

    @Override
    public String toString() {
        return "InputArgs{" +
                "pictureSource=" + pictureSource +
                ", pictureType=" + pictureType +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputArgs inputArgs = (InputArgs) o;
        return pictureSource == inputArgs.pictureSource && pictureType == inputArgs.pictureType && Objects.equals(path, inputArgs.path);
    }

}
