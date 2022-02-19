package io.args;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Args {
    /**
     * Метод, осуществляющий загрузку параметров вывода из файла , или создание параметров вывода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */
    public static OutputArgs loadOutArgs(String path) {
        Gson g= new Gson();
        try {
            FileReader fileReader = new FileReader(new File(path));
            return g.fromJson("h", OutputArgs.class);
        } catch (FileNotFoundException e) {
           return null;
        }
    }
    /**
     * Метод, осуществляющий загрузку параметров ввода из файла , или создание параметров ввода по умолчанию
     * @param path - путь к файлу с параметрами вывода
     */
    public static InputArgs loadInArgs(String path) {
        Gson g= new Gson();
        try {
            FileReader fileReader = new FileReader(new File(path));
            return g.fromJson("117013", InputArgs.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
