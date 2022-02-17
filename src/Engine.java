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

import java.util.Scanner;

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
     * Поле минимального периода между обработкой двух соседних кадров
     */
    private final double CHANGE_PERIOD = 1 / 30.0;

    /**
     * Экземпляр потока, в котором работает программа
     */
    private Thread thread;

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

    /**
     * Экземпляр класса типа{@link Scanner} для чтения информации из консоли
     */
    private Scanner sc;

    public static void main(String args[]) {
        Engine engine = new Engine();
        engine.loadInputArgs("PATH");
        engine.loadOutputArgs("PATH");
        engine.start();
    }

    /**
     * Метод, осуществляющий загрузку параметров ввода из файла , или создание параметров ввода по умолчанию
     * @param path - путь к файлу с параметрами ввода
     * @return возвращает объект типа {@link InputArgs}, хранящий параметры ввода
     */
    public InputArgs loadInputArgs(String path){
        return null;
    }
    /**
     * Метод, осуществляющий загрузку параметров вывода из файла , или создание параметров вывода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     * @return возвращает объект типа {@link OutputArgs}, хранящий параметры вывода
     */
    public OutputArgs loadOutputArgs(String path){
        return null;
    }

    /**
     * Метод, запускающий работу основного потока программы.
     * Создает поля {@link Engine#input},{@link Engine#output},{@link Engine#mapMain},{@link Engine#vecMain},{@link Engine#edgMain} и {@link Engine#sc}
     */
    public void start() {
        thread = new Thread(this);

        input=new Input(inputArgs);
        output=new Output(outputArgs);
        mapMain=new MapMain();
        vecMain=new VecMain();
        edgMain=new EdgMain();
        sc=new Scanner(System.in);

        thread.run();
    }

    /**
     * Основной метод работы программы.
     * Осуществляет отсчет времени для обработки следующих кадров. Взаимодействие модулей программы между собой и с пользователем.
     */
    public void run(){

        boolean render = false; //is rendering needed
        double time = System.nanoTime() / 1000_000_000.0; //current time
        double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
        double elapsedTime = 0.0; // elapsed time after last program's tick
        double unrenderedTime = 0.0; // elapsed time after last render
        double consoleTime =0.0; //elapsed time after last message in console
        running = true;

        Picture picture;
        Edges edges;
        Figures figures;
        Map map;
        String inputMessage;

        while (running) {
            /*calculating time*/
            render = false;
            time = System.nanoTime() / 1000_000_000.0;
            elapsedTime = time - lastTime;
            lastTime = time;
            unrenderedTime += elapsedTime;
            consoleTime += elapsedTime;

            /*
             *Вывод параметров ввода и вывода программы каждые 30 секунд
             */
            if(consoleTime>30.0){
                consoleTime=0.0;
                System.out.println(input.getArgs());
                System.out.println(output.getArgs());
            }
            if(unrenderedTime>=CHANGE_PERIOD){
                unrenderedTime=0;

                inputMessage=sc.nextLine(); //message from user's console
                if(inputMessage!=null||inputMessage.length()<2) {
                    switch (inputMessage.substring(0,2)){
                        case "-e":
                            break;
                    }
                }
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
        System.exit(0);
    }

}
