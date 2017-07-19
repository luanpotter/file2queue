package xyz.luan.file2queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class Files {

    public static Stream<String> lines(String fileName) {
        try {
            return new BufferedReader(new FileReader(fileName)).lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
