package xyz.luan.file2queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class AwsQueue implements Queue {

    private String queueUrl;
    private AmazonSQS sqs;

    public AwsQueue(String queueUrl) {
        String[] parts = queueUrl.split("@");
        this.queueUrl = parts[1];
        String[] p2 = parts[0].split(":");
        System.setProperty("aws.accessKeyId", p2[0]);
        System.setProperty("aws.secretKey", p2[1]);
        this.sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-2").build();
    }

    @Override
    public void send(String message) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message)
                .withDelaySeconds(0);
        sqs.sendMessage(send_msg_request);
    }

    @Override
    public void close() {
    }
}
