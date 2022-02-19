package engine;

import io.args.Args;
import io.args.InputArgs;
import io.args.OutputArgs;
import java.util.Scanner;

/**
 * Класс, отвечающий за ввод команд из консоли, их проверку на корректность, отправки их другим модулям программы и вывод сообщений в консоль. Обладает своим собственным таймером.
 */
public class Console extends Thread{
    private Scanner scanner;
    private Args args;
    private Thread consoleTime;
    public boolean running;

    /**
     * Конструктор для создания консоли.
     * @param eng ссылка на объект типа {@link Engine}, к которому будет привязана консоль.
     * Извлекает из eng ссылки на объекты {@link OutputArgs} и {@link  InputArgs}
     */
    public Console(Engine eng){
        scanner=new Scanner(System.in);
        args=eng.getArgs();
        consoleTime=new Thread(new ConsoleTime());
    }

    /**
     * Запускает внутренний таймер в новом потоке и собственный поток для работы.
     */
    public void startRunning(){
        running=true;
        consoleTime.start();
        start();
    }

    /**
     * Останавливает работу основного потока и потока таймера.
     */
    public void stopRunning(){
        running=false;
    }

    /**
     * Главный рабочий метод класса. Осуществляет чтение, вывод и рассылку сообщений.
     */
    public void run(){
        String message=null;
        while (running){
            message=scanner.nextLine();
            if(message!=null&&message.length()>=2) {
                switch (message.substring(0,2)){
                    case "-e":
                        running=false;
                        break;
                    case "-h":
                        System.out.println(args.help());
                        break;
                    case "-i":
                        args.command(message);
                        break;
                    case "-o":
                        args.command(message);
                        break;
                    default:
                        System.out.println("Wrong command");
                        System.out.println(args.help());
                }
            }else{
                System.out.println("Wrong command");
                System.out.println(args.help());
            }
        }
    }

    /**
     * Внутренний класс таймера консоли. Осуществляет отсчет времени работы консоли.
     */
    class ConsoleTime implements Runnable{
        private double infoPeriod=5.0;

        @Override
        public void run() {
            double time = 0.0; //current time
            double lastTime = System.nanoTime() / 1000_000_000.0; //last got time
            double elapsedTime = 0.0; // elapsed time after last program's tick
            double consoleTime = 0.0; // elapsed time after last info message
            while (running) {
                time = System.nanoTime() / 1_000_000_000.0;
                elapsedTime = time - lastTime;
                lastTime = time;
                consoleTime += elapsedTime;
                if (consoleTime > infoPeriod) {
                    System.out.println(args.info());
                    consoleTime=0.0;
                }
            }
        }
    }
}