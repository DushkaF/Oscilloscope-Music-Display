package engine;

import factory.Picture;
import factory.edges.EdgePicture;
import factory.map.Map;
import factory.vectors.VectorPicture;
import io.args.Args;
import io.debug.DebugWindow;
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
    private Thread debugThread;

    private DebugWindow debugWindow;

    private Input input;
    private Output output;


    private Console console;
    private EdgMain edgMain;
    private VecMain vecMain;
    private MapMain mapMain;

    public Picture picture;
    protected Args args;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
    }

    public Engine() {
        args=new Args(this);
        edgMain=new EdgMain();
        vecMain=new VecMain();
        mapMain=new MapMain();
    }

    public void start() {
        running = true;

        picture=new Picture();
        console=new Console(this);
        input=new Input(args.inputArgs);
        output=new Output(args.outputArgs);
        consoleThread=new Thread(console, "CONSOLE_THREAD");
        engineThread=new Thread(this, "ENGINE_THREAD");
        consoleThread.setDaemon(true);
        engineThread.start();
        consoleThread.start();
    }

    @Override
    public void run() {
        double time = 0.0; //current time
        double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
        double elapsedTime = 0.0; // elapsed time after last program's tick
        double unrenderedTime = 0.0; // elapsed time after last render
        Map map = null;
        byte framesPassed=0;
        double frameTime=0;
        EdgePicture edgePicture;
        VectorPicture vectorPicture;
        while(running){
            /*calculating time*/
            time = System.nanoTime() / 1_000_000_000.0;
            elapsedTime = time - lastTime;
            lastTime = time;
            unrenderedTime += elapsedTime;
            frameTime+=elapsedTime;
            if(frameTime>1.0){
                frameTime=0;
                picture.fps=framesPassed;
                framesPassed=0;
            }
            if(unrenderedTime>=CHANGE_PERIOD){
                unrenderedTime=0;
                input.getPicture(picture);
                if(picture.isNew){
                edgePicture=edgMain.getEdges(picture, args.editArgs);
                vectorPicture=vecMain.getFigures(edgePicture);
                map=mapMain.getMap(vectorPicture);
                output.draw(map);
                picture.debugRendered=false;
                }
                framesPassed++;
                picture.isNew=false;
            }
        }
        args.command("-s i cfg/input/last_input_args");
        args.command("-s o cfg/output/last_output_args");
        args.command("-s d cfg/debug/last_debug_args");
        args.command("-s e cfg/edit/last_edit_args");
    }

    public void openDebug() {
        debugWindow=new DebugWindow(picture, args.debugArgs);
        debugThread = new Thread(debugWindow);
        debugThread.setDaemon(true);
        debugThread.setName("DEBUG_THREAD");
        debugThread.start();
    }
}
