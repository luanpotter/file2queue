package xyz.luan.file2queue;

public class StopWatch {

    private int count;
    private long ms;

    public StopWatch() {
        this.count = 0;
        this.ms = System.currentTimeMillis();
    }

    public void tick() {
        count++;
        if (count % 100 == 0) {
            long c = System.currentTimeMillis();
            long diff = c - ms;
            System.out.println("Current count: " + count + " | Diff: " + diff);
            ms = c;
        }
    }
}
