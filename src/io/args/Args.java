package io.args;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Args {
    public InputArgs inputArgs;
    public OutputArgs outputArgs;

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

    public void command(String message) {

    }

    public String help() {
        return "If you see this, then I forgot to do help message";
    }

    public String info() {
        return ("IOArgs info: \n"+inputArgs+"\n"+outputArgs);
    }
}
