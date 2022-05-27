package io.output;

import factory.vectors.Vector;
import io.args.OutputArgs;


import java.util.LinkedList;

public class Output implements Runnable{
    private OutputArgs outputArgs;
    private Thread outputThread;
    public Output(OutputArgs outputArgs) {
        this.outputArgs=outputArgs;
    }

    public void draw(LinkedList<Vector> map) {
    }

    public OutputArgs getArgs() {
        return outputArgs;
    }

    public void start() {
        outputThread=new Thread(this);
        outputThread.setName("OUTPUT_THREAD");
        outputThread.run();
    }

    @Override
    public void run() {

    }
}
