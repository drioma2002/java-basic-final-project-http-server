package ru.otus.http.server.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    private static final File folder = new File("static");

    public static boolean fileExists(String name) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] readFile(String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(folder + "/" + fileName))) {
            return in.readAllBytes();
        } catch (IOException e) {
            return null;
        }
    }
}
