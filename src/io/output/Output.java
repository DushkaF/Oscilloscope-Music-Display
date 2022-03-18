package io.output;

import io.args.OutputArgs;
import factory.map.Map;

public class Output implements Runnable{
    private OutputArgs outputArgs;
    private Thread outputThread;
    public Output(OutputArgs outputArgs) {
        this.outputArgs=outputArgs;
    }

    public void draw(Map map) {
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
