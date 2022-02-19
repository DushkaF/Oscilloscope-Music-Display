package io.args;

public class OutputArgs {
    @Override
    public String toString() {
        return "OutputArgs{" +
                "isActive=" + isActive +
                ", savePath='" + savePath + '\'' +
                '}';
    }

    public boolean isActive;
    public String savePath;
}
