package xyz.luan.file2queue;

public interface Queue extends AutoCloseable {
    void send(String message);

    @Override
    void close();
}
