package xyz.luan.file2queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AwsQueueFifo implements Queue {

    private String queueUrl;
    private AmazonSQS sqs;
    private List<SendMessageBatchRequestEntry> messages;
    private AtomicInteger id = new AtomicInteger(0);

    public AwsQueueFifo(String queueUrl) {
        String[] parts = queueUrl.split("@");
        this.queueUrl = parts[1];
        String[] p2 = parts[0].split(":");
        System.setProperty("aws.accessKeyId", p2[0]);
        System.setProperty("aws.secretKey", p2[1]);
        this.sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-2").build();
        this.messages = new ArrayList<>();
    }

    @Override
    public void send(String message) {
        SendMessageBatchRequestEntry entry = new SendMessageBatchRequestEntry();
        entry.setDelaySeconds(0);
        entry.setMessageGroupId("salsixa");
        entry.setMessageBody(message);
        entry.setMessageDeduplicationId(String.valueOf(Base64.getEncoder().encode(message.getBytes())));
        entry.setId("message-id-" + id.getAndIncrement());

        this.messages.add(entry);
        if (this.messages.size() == 10) {
            sendAll();
        }
    }

    private void sendAll() {
        SendMessageBatchRequest req = new SendMessageBatchRequest();
        req.withEntries(messages);
        req.withQueueUrl(queueUrl);
        sqs.sendMessageBatch(req);
        messages.clear();
    }

    @Override
    public void close() {
        sendAll();
    }


    // Test code
    public static void main(String[] args) throws IOException {
        String[] arguments = { "--type", "AWS_FIFO", "--path", "/Users/fabiano/2queue", "--url", "AKIAIPJ2SNB26DQ5MQRA:US9NLJMYiH8Mt1uIjGxtY9QtwUx9uw3W/XoUis0r@https://sqs.us-east-2.amazonaws.com/048639382555/crawler2-input.fifo" };
        Main.main(arguments);
    }
}
