package xyz.luan.file2queue;

import org.fusesource.jansi.AnsiConsole;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.ansi;
import static org.kohsuke.args4j.OptionHandlerFilter.ALL;
import static xyz.luan.file2queue.Options.Type.JMS;

public class Options {

    @Option(name = "--type", usage = "The destination type, may be JMS or PUBSUB")
    private Type type = Type.JMS;

    @Option(name = "--path", usage = "The path of the input file with the new line separated messages")
    private String inputFile;

    @Option(name = "--url", usage = "The project on pubsub or the broker url for JMS")
    private String url;

    @Option(name = "--queue", usage = "The name of the queue or topic")
    private String queue;

    @Option(name = "--credentials", usage = "The path of a google credentials file")
    private String credentialsFile;

    @Option(name = "--limit", usage = "Limit the file")
    private long limit = Long.MAX_VALUE;

    public static Options create(String[] args) {
        Options options = new Options();
        if (args.length == 0) {
            options.readStdin();
        } else {
            options.readArgs(args);
        }
        return options;
    }

    public void readArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java file2queue [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java file2queue" + parser.printExample(ALL));
            System.exit(1);
        }
    }

    public void readStdin() {
        AnsiConsole.systemInstall();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, friend!");

        System.out.print(ansi().fg(GREEN).a("What's the type of the queue? [JMS, PUBSUB] ").reset());
        System.out.flush();
        type = Type.valueOf(scanner.nextLine());
        System.out.println();

        System.out.print(ansi().fg(GREEN).a("What's the type of the input file path? ").reset());
        System.out.flush();
        inputFile = scanner.nextLine();
        System.out.println();

        System.out.print(ansi().fg(GREEN).a(type == JMS ? "What's the broker url? " : "What's the project id on gcloud? ").reset());
        System.out.flush();
        url = scanner.nextLine();
        System.out.println();

        System.out.print(ansi().fg(GREEN).a(type == JMS ? "What's the queue name? " : "What's the topic id? ").reset());
        System.out.flush();
        queue = scanner.nextLine();
        System.out.println();

        if (type != JMS) {
            System.out.print(ansi().fg(GREEN).a("Where's the credentials file? ").reset());
            System.out.flush();
            credentialsFile = scanner.nextLine();
            System.out.println();
        }
    }

    public Queue getQueue() {
        switch (type) {
            case JMS:
                return new JmsQueue(url, queue);
            case PUBSUB:
                return new PubSubQueue(url, queue, credentialsFile);
            case AWS:
                return new AwsQueue(url);
            case AWS_FIFO:
                return new AwsQueueFifo(url);
        }
        throw new RuntimeException("Invalid type: " + type);
    }

    public String getInputFile() {
        return inputFile;
    }

    public long getLimit() {
        return limit;
    }

    public enum Type {
        JMS, PUBSUB, AWS, AWS_FIFO
    }
}
