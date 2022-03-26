package io.args;

import io.args.arg_type.DebugPictureType;

import java.util.Arrays;

public class DebugArgs {
    public DebugPictureType[] pictureList;

    public DebugArgs(){
        pictureList=new DebugPictureType[4];
        pictureList[0]=DebugPictureType.RAW;
        pictureList[1]=DebugPictureType.GREY;
        pictureList[2]=DebugPictureType.EDGED;
        pictureList[3]=DebugPictureType.FIGURES;
    }
    public byte command(String message){
        if(message.length()<4){
           return -1;
        }else{
            switch (message.substring(3)){
                case "o":
                    return 1;
                default:
                    String m[]=message.substring(3).split(" ");
                    if(m.length%2!=0)
                        return -1;
                    for (int i = 0; i < m.length; i+=2) {
                        try {
                            DebugPictureType type=DebugPictureType.valueOf(m[i]);
                            short sh=Short.parseShort(m[i+1]);
                            if(Short.parseShort(m[i+1])<0||Short.parseShort(m[i+1])>3)
                                return -1;
                            pictureList[sh]=type;
                        }catch (IllegalArgumentException e){
                            return -1;
                        }
                    }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DebugArgs{" +
                "pictureList=" + Arrays.toString(pictureList) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebugArgs debugArgs = (DebugArgs) o;
        return Arrays.equals(pictureList, debugArgs.pictureList);
    }

}
