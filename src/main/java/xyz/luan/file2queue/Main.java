package xyz.luan.file2queue;

public class Main {

    public static void main(String[] args) {
        String path = args[0];
        String urlBroker = args[1];
        String queueName = args[2];

        try (Queue p = new Queue(urlBroker, queueName)) {
            Files.lines(path).forEach(p::send);
        }
    }
}
