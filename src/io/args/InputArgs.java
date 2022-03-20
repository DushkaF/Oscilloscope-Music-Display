package io.args;

import io.args.arg_type.PictureSource;
import io.args.arg_type.PictureType;

public class InputArgs {
    @Override
    public String toString() {
        return "InputArgs{" +
                "pictureSource=" + pictureSource +
                ", pictureType=" + pictureType +
                ", path='" + path + '\'' +
                '}';
    }

    public PictureSource pictureSource;
    public PictureType pictureType;
    public String path;

    public byte command(String message) {
        return 0;
    }
}
