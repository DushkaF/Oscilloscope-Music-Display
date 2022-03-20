package io.args;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import engine.Engine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Args {
    public InputArgs inputArgs;
    public OutputArgs outputArgs;
    public DebugArgs debugArgs;
    private Engine engine;
    /**
     * Метод, осуществляющий загрузку параметров вывода из файла , или создание параметров вывода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */

    public static OutputArgs loadOutArgs(String path) {
        Gson g= new Gson();
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s.toString(), OutputArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            return createOutArgs();
        }
    }
    /**
     * Метод, осуществляющий загрузку параметров ввода из файла , или создание параметров ввода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */
    public static InputArgs loadInArgs(String path) {
        Gson g= new Gson();
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s.toString(), InputArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            return createInArgs();
        }
    }

    private static InputArgs createInArgs(){
        return null;
    }
    private static OutputArgs createOutArgs(){
        return null;
    }

    public Args(Engine engine){
        outputArgs = Args.loadOutArgs("cfg/output/last_output_args.txt");
        inputArgs = Args.loadInArgs("cfg/input/last_input_args.txt");
        debugArgs=new DebugArgs();
        this.engine=engine;
    }

    public void command(String message) {
        switch (message.substring(0, 2)) {
            case "-d":
                switch (debugArgs.command(message)){
                    case -1:
                        System.out.println("Wrong command");
                        System.out.println(help());
                        break;
                    case 1:
                        engine.openDebug();
                        break;
                    case 0:
                        break;
                }
                break;
            case "-i":
              switch(inputArgs.command(message)) {
                case -1:
                    System.out.println("Wrong command");
                    System.out.println(help());
                    break;
                case 1:

                    break;
                case 0:
                    break;
            }
                break;
            case "-o":
                switch(outputArgs.command(message)) {
                    case -1:
                        System.out.println("Wrong command");
                        System.out.println(help());
                        break;
                    case 1:

                        break;
                    case 0:
                        break;
                }
                break;
        }
    }

    public String help() {
        return "If you see this, then I forgot to do help message";
    }

    public String info() {
        return ("IOArgs info: \n"+inputArgs+"\n"+outputArgs+"\n Debug info: \n"+debugArgs);
    }
}
