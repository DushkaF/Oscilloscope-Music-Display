package engine;

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

/**
 * Главный класс, осуществляющий связь всех компонентов программы
 */
public class Engine implements Runnable{

    /**
     * Поле аргументов чтения
     */
    private InputArgs inputArgs;

    /**
     * Поле аргументов вывода
     */
    private OutputArgs outputArgs;

    /**
     * Поле, определяющее осуществляется ли работа основного цикла программы
     */
    private boolean running = false;

    /**
     * Поле минимального периода между обработкой двух кадров
     */
    private final double CHANGE_PERIOD = 1 /30.0;

    /**
     * Экземпляр модуля типа {@link Console}, отвечающего за чтение и вывод в консоль
     */
    private Console console;

    /**
     * Экземпляр модуля ввода изображения типа {@link Input}
     */
    private Input input;

    /**
     * Экземпляр модуля вывода изображения на осциллограф типа {@link Output}
     */
    private Output output;

    /**
     * Экземпляр модуля типа {@link VecMain} для преобразования растра в векторы
     */
    private VecMain vecMain;

    /**
     * Экземпляр модуля создания карты вывода типа {@link MapMain}
     */
    private MapMain mapMain;

    /**
     * Экземпляр модуля типа {@link EdgMain} для первичной обработки изображения (выделения краев).
     */
    private EdgMain edgMain;

    public static void main(String args[]) {
        Engine engine = new Engine();
        engine.init();
        engine.run();
    }

    /**
     * Основной метод работы программы.
     * Осуществляет отсчет времени для обработки следующих кадров. Взаимодействие модулей программы между собой и с пользователем.
     */
    public void run(){
        double time = 0.0; //current time
        double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
        double elapsedTime = 0.0; // elapsed time after last program's tick
        double unrenderedTime = 0.0; // elapsed time after last render
        running = true;
        console.startRunning();

        Picture picture;
        Edges edges;
        Figures figures;
        Map map;

        while (running&& console.running) {
            /*calculating time*/
            time = System.nanoTime() / 1_000_000_000.0;
            elapsedTime = time - lastTime;
            lastTime = time;
            unrenderedTime += elapsedTime;
            if(unrenderedTime>=CHANGE_PERIOD){
                unrenderedTime=0;
                picture=input.getPicture();
                edges=edgMain.getEdges(picture);
                figures=vecMain.getVectors(edges);
                map=mapMain.getMap(figures);
                output.draw(map);

            }

        }
        end();
    }

    /**
     * Метод завершения программы. Осуществляет сохранение необходимых полей, объектов и т.д.
     */
    public void end() {
        console.stopRunning();
    }

    /**
     * Инициализирует необходимые классы для взаимодействия с другими частями программы: {@link Engine#input},{@link Engine#output},{@link Engine#mapMain},{@link Engine#vecMain}, {@link Engine#edgMain} и {@link Engine#console}.
     */
    public void init() {
        loadInputArgs("PATH");
        loadOutputArgs("PATH");
        input=new Input(inputArgs);
        output=new Output(outputArgs);
        mapMain=new MapMain();
        vecMain=new VecMain();
        edgMain=new EdgMain();
        console=new Console(this);
    }

    /**
     * Метод, осуществляющий загрузку параметров ввода из файла , или создание параметров ввода по умолчанию
     * @param path путь к файлу с параметрами ввода
     */
    public void loadInputArgs(String path){
        inputArgs=new InputArgs(path);
    }

    /**
     * Метод, осуществляющий загрузку параметров вывода из файла , или создание параметров вывода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */
    public void loadOutputArgs(String path){
        outputArgs=new OutputArgs(path);
    }

    public InputArgs getInputArgs() {
        return inputArgs;
    }

    public OutputArgs getOutputArgs() {
        return outputArgs;
    }
}
