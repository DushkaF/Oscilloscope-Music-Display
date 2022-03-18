package io.input;

import engine.Picture;
import io.args.InputArgs;

public class Input implements Runnable{
    private InputArgs inputArgs;
    private Thread inputThread;
    public Input(InputArgs inputArgs) {
        this.inputArgs=inputArgs;
    }

    public Picture getPicture() {
        return null;
    }

    public String input(String nextLine) {
        return null;
    }

    public InputArgs getArgs() {
        return inputArgs;
    }

    public void start() {
        inputThread=new Thread(this);
        inputThread.setName("INPUT_THREAD");
        inputThread.setDaemon(true);
        inputThread.run();
    }

    @Override
    public void run() {

    }
}
