package io.args;

public class OutputArgs {
    public double sampleBits = 8;   //TODO

    @Override
    public String toString() {
        return "OutputArgs{" +
                "isActive=" + isActive +
                ", savePath='" + savePath + '\'' +
                '}';
    }

    public boolean isActive;
    public String savePath;

    public byte command(String message) {
        return 0;
    }
}
