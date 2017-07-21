package xyz.luan.file2queue;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Options options = Options.create(args);
        StopWatch watch = new StopWatch();
        try (Queue p = options.getQueue()) {
            Files.lines(options.getInputFile()).limit(options.getLimit()).forEach(message -> {
                p.send(message);
                watch.tick();
            });
        }
    }
}
