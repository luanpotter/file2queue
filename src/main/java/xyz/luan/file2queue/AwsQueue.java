package xyz.luan.file2queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AwsQueue implements Queue {

    private String queueUrl;
    private AmazonSQS sqs;
    private List<String> messages;
    private AtomicInteger id = new AtomicInteger(0);

    public AwsQueue(String queueUrl) {
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
        this.messages.add(message);
        if (this.messages.size() == 10) {
            sendAll();
        }
    }

    private void sendAll() {
        SendMessageBatchRequest req = new SendMessageBatchRequest();
        req.withEntries(messages.stream().map(s -> new SendMessageBatchRequestEntry("message-id" + id.getAndIncrement(), s)).collect(Collectors.toList()));
        req.withQueueUrl(queueUrl);
        sqs.sendMessageBatch(req);
        messages.clear();
    }

    @Override
    public void close() {
        sendAll();
    }
}
