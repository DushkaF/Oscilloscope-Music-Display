package engine;

import factory.edges.Edges;
import factory.map.Map;
import factory.vectors.Figures;
import io.args.Args;
import io.input.Input;
import io.output.Output;
import factory.edges.EdgMain;
import factory.map.MapMain;
import factory.vectors.VecMain;

public class Engine implements Runnable {
    protected boolean running;
    private final double CHANGE_PERIOD = 1 / 30.0;

    private Thread engineThread;
    private Thread consoleThread;
    private Thread inputThread;
    private Thread outputThread;
    private Thread debugThread;
    private DebugWindow debugWindow;
    private Input input;
    private Output output;
    private Picture picture;
    private Console console;
    private EdgMain edgMain;
    private VecMain vecMain;
    private MapMain mapMain;
    protected Args args;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
    }

    public Engine() {
        args=new Args();
        args.outputArgs = Args.loadOutArgs("cfg/output/last_output_args.txt");
        args.inputArgs = Args.loadInArgs("cfg/input/last_input_args.txt");
        edgMain=new EdgMain();
        vecMain=new VecMain();
        mapMain=new MapMain();
    }

    public void start() {
        running = true;

        console=new Console(this);
        input=new Input(args.inputArgs);
        output=new Output(args.outputArgs);
        consoleThread=new Thread(console, "CONSOLE_THREAD");
        engineThread=new Thread(this, "ENGINE_THREAD");
        engineThread.start();
        consoleThread.start();
        /*
        inputThread=new Thread(input);
        outputThread=new Thread(output);



        inputThread.start();
        outputThread.start();
        */
    }

    @Override
    public void run() {
        double time = 0.0; //current time
        double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
        double elapsedTime = 0.0; // elapsed time after last program's tick
        double unrenderedTime = 0.0; // elapsed time after last render
        Edges edges;
        Figures figures;
        Map map;
        picture=new Picture();
        while(running){
            /*calculating time*/
            time = System.nanoTime() / 1_000_000_000.0;
            elapsedTime = time - lastTime;
            lastTime = time;
            unrenderedTime += elapsedTime;
            if(unrenderedTime>=CHANGE_PERIOD*100){
                unrenderedTime=0;
                System.out.println("RENDER");
                //picture= input.getPicture();
                edges= edgMain.getEdges(picture);
                figures=vecMain.getFigures(edges);
                map=mapMain.getMap(figures);
                output.draw(map);
                if(debugThread.isAlive()){
                    debugWindow.setPicture(picture);
                }
            }
        }
    }

    public void openDebug() {
        debugWindow=new DebugWindow();
        debugThread = new Thread(debugWindow);
        debugThread.setDaemon(true);
        debugThread.setName("DEBUG_THREAD");
        debugWindow.setPicture(picture);
        debugThread.start();
    }
}
