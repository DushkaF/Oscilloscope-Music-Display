import edges.EdgMain;
import edges.Edges;
import input.Input;
import input.InputArgs;
import input.Picture;
import map.Map;
import map.MapMain;
import output.Output;
import output.OutputArgs;
import vectors.VecMain;
import vectors.Figures;

public class Engine implements Runnable{
    private InputArgs inputArgs;
    private OutputArgs outputArgs;
    private boolean running = false; // is game running
    private final double CHANGE_PERIOD = 1 / 30.0; // minimum time between two rendering frames
    private Thread thread; // engine thread

    public static void main(String args[]) {
        Engine engine = new Engine();
        engine.loadInputArgs();
        engine.loadOutputArgs();
        engine.start();
    }

    public InputArgs loadInputArgs(){
        return null;
    }
    public OutputArgs loadOutputArgs(){
        return null;
    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    public void run(){

        boolean render = false; //is rendering needed
        double time = System.nanoTime() / 1000_000_000.0; //current time
        double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
        double elapsedTime = 0.0; // elapsed time from last program's tick
        double unrenderedTime = 0.0; // elapsed time from last render
        running = true;

        Input input=new Input(inputArgs);
        Output output=new Output(outputArgs);
        MapMain mapMain=new MapMain();
        VecMain vecMain=new VecMain();
        EdgMain edgMain=new EdgMain();

        Picture picture;
        Edges edges;
        Figures figures;
        Map map;
        while (running) {
            /*calculating time*/
            render = false;
            time = System.nanoTime() / 1000_000_000.0;
            elapsedTime = time - lastTime;
            lastTime = time;
            unrenderedTime += elapsedTime;

            if(unrenderedTime>=CHANGE_PERIOD){
                unrenderedTime=0;

                picture=Input.getPicture();
                edges=edgMain.getEdges(picture);
                figures=VecMain.getVectors(edges);
                map=mapMain.getMap(figures);
                output.draw(map);
            }
        }
        end();
    }

    public void end() {

    }

}
