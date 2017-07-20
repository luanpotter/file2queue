package xyz.luan.file2queue;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Options options = readOptions(args);

        try (Queue p = options.getQueue()) {
            Files.lines(options.getInputFile()).forEach(p::send);
        }
    }

    private static Options readOptions(String[] args) {
        Options options = new Options();
        if (args.length == 0) {
            options.readStdin();
        } else {
            options.readArgs(args);
        }
        return options;
    }
}
