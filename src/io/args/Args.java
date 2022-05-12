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
    public EditArgs editArgs;

    private Engine engine;
    private static Gson g;
    /**
     * Метод, осуществляющий загрузку параметров вывода из файла , или создание параметров вывода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */

    public OutputArgs loadOutArgs(String path, boolean createNew ) {
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s, OutputArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            if (createNew) {
                return new OutputArgs();
            }else {
                return outputArgs;
            }
        }
    }

    public EditArgs loadEditArgs(String path, boolean createNew ) {
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s, EditArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            if (createNew) {
                return new EditArgs();
            }else {
                return editArgs;
            }
        }
    }

    /**
     * Метод, осуществляющий загрузку параметров ввода из файла , или создание параметров ввода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */
    public InputArgs loadInArgs(String path, boolean createNew) {
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s, InputArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            if (createNew) {
                return new InputArgs();
            }else {
                return inputArgs;
            }
        }
    }
    public DebugArgs loadDebugArgs(String path, boolean createNew) {
        try {
            String s= Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return g.fromJson(s, DebugArgs.class);
        } catch (IOException | JsonSyntaxException e) {
            if (createNew) {
                return new DebugArgs();
            }else {
                return debugArgs;
            }
        }
    }
    public Args(Engine engine){
        g= new Gson();
        outputArgs = loadOutArgs("cfg/output/last_output_args.json", true);
        inputArgs = loadInArgs("cfg/input/last_input_args.json", true);
        debugArgs= loadDebugArgs("cfg/debug/last_debug_args.json", true);
        editArgs=loadEditArgs("cfg/edit/last_edit_args.json", true);
        this.engine=engine;
    }

    public void command(String message) {
        switch (message.substring(0, 2)) {
            //debug command
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
                        System.out.println("new debugArgs applied: "+debugArgs);
                        engine.picture.isNew=true;
                        break;
                }
                break;
            //input command
            case "-i":
              switch(inputArgs.command(message)) {
                case -1:
                    System.out.println("Wrong command");
                    System.out.println(help());
                    break;
                case 1:

                    break;
                case 0:
                    System.out.println("new inputArgs applied: "+inputArgs);
                    break;
            }
                break;
              //edit command
            case "-e":
                switch(editArgs.command(message)) {
                    case -1:
                        System.out.println("Wrong command");
                        System.out.println(help());
                        break;
                    case 1:

                        break;
                    case 0:
                        System.out.println("new editArgs applied: "+editArgs);
                        engine.picture.isNew=true;
                        break;
                }
                break;
            //output command
            case "-o":
                switch(outputArgs.command(message)) {
                    case -1:
                        System.out.println("Wrong command");
                        System.out.println(help());
                        break;
                    case 1:

                        break;
                    case 0:
                        System.out.println("new outputArgs applied: "+outputArgs);
                        break;
                }
                break;

            //saving command
            case "-s":
                String arrS[]=message.substring(3).split(" ");
                if(arrS.length==2){
                    File file = new File(arrS[1]+".json");
                    try {
                        file.createNewFile();
                        FileWriter fileWriter=new FileWriter(file);
                        switch (arrS[0]){
                            case "i":
                                fileWriter.write(g.toJson(inputArgs));
                                System.out.println("input args saved successful");
                                break;
                            case "o":
                                fileWriter.write(g.toJson(outputArgs));
                                System.out.println("output args saved successful");
                                break;
                            case "d":
                                fileWriter.write(g.toJson(debugArgs));
                                System.out.println("debug args saved successful");
                                break;
                            case "e":
                                fileWriter.write(g.toJson(editArgs));
                                System.out.println("edit args saved successful");
                                break;
                        }
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        System.out.println("Wrong path");
                    }
                }
                break;
            //loading command
            case "-l":
                String arrL[]=message.substring(3).split(" ");
                if(arrL.length==2){
                        switch (arrL[0]){
                            case "i":
                                if(loadInArgs(arrL[1], false).equals(inputArgs)){
                                    System.out.println("Wrong path");
                                }else{
                                    inputArgs=loadInArgs(arrL[1], false);
                                    System.out.println("loading succesful: "+inputArgs);
                                }
                                break;
                            case "o":
                                if(loadOutArgs(arrL[1], false).equals(outputArgs)){
                                    System.out.println("Wrong path");
                                }else{
                                    outputArgs=loadOutArgs(arrL[1], false);
                                    System.out.println("loading succesful: "+outputArgs);
                                }
                                break;
                            case "d":
                                if(loadDebugArgs(arrL[1], false).equals(debugArgs)){
                                    System.out.println("Wrong path");
                                }else{
                                    debugArgs=loadDebugArgs(arrL[1], false);
                                    System.out.println("loading succesful: "+debugArgs);
                                }
                                break;
                            case "e":
                                if(loadEditArgs(arrL[1], false).equals(editArgs)){
                                    System.out.println("Wrong path");
                                }else{
                                    editArgs=loadEditArgs(arrL[1], false);
                                    System.out.println("loading succesful: "+editArgs);
                                }
                                break;
                            default:
                                System.out.println("Wrong command");
                                System.out.println(help());
                                break;
                        }
                }
                break;
        }
    }

    public String help() {
        return "If you see this, then I forgot to do help message";
    }

    public String info() {
        return ("IOArgs info: \n"+inputArgs+"\n"+outputArgs+"\nEdit info: \n"+editArgs+"\n"+"Debug info: \n"+debugArgs);
    }
}
