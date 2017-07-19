# file2queue

Very simple file to queue utility. Usage:

```
    mvn clean install
    mvn exec:java -Dexec.args="FILE URL QUEUE"
```

Or generate a jar on clean install:

```
  mvn clean install
  cp target/file2queue-1.0-SNAPSHOT-jar-with-dependencies.jar file2queue.jar
  java -jar file2queue.jar FILE URL QUEUE
```

## Examples

Examples of the parameters:

 * FILE : path for a file with the messages to be sent, each line is a distinct message.
 * URL : url of the queue with auth, example: tcp://USER:PASS@IP:61616?jms.useAsyncSend=true
 * QUEUE : name of the queue (it will be created if non-existent, must be a queue)

Example of the file:

```
message 1
message 2, with spaces
```

## TODO

Won't sent messages with newlines, because there is no escape.
