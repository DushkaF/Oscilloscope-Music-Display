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
            if (message != null && message.length() >= 3) {
                switch (message.substring(0, 3)) {
                    case "-ex":
                        engine.running = false;
                        break;
                    case "-h ":
                        System.out.println(engine.args.help());
                        break;
                    case "-i ":
                    case "-d ":
                    case "-o ":
                    case "-s ":
                    case "-l ":
                    case "-e ":
                        engine.args.command(message);
                        break;
                    case "-in":
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