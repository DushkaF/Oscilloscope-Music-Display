package engine;

import java.util.Scanner;

class Console implements Runnable {
    private Engine engine;
    private String message;
    private Scanner scanner;

    public Console(Engine engine){
        scanner=new Scanner(System.in);
        this.engine=engine;
    }

    @Override
    public void run() {

        System.out.println(engine.args.info());
        while (engine.running) {
            message = scanner.nextLine();
            if (message != null && message.length() >= 2) {
                switch (message.substring(0, 2)) {
                    case "-d":
                        engine.openDebug();
                        break;
                    case "-e":
                        engine.running = false;
                        break;
                    case "-h":
                        System.out.println(engine.args.help());
                        break;
                    case "-in":
                    case "-o":
                        engine.args.command(message);
                        break;
                    case "-i":
                        System.out.println(engine.args.info());
                        break;
                    default:
                        System.out.println("Wrong command");
                        System.out.println(engine.args.help());
                }
            } else {
                System.out.println("Wrong command");
                System.out.println(engine.args.help());
            }
        }
    }

}