package xyz.luan.file2queue.pubsub;

import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.grpc.ExecutorProvider;
import com.google.api.gax.grpc.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.SubscriptionName;

import java.io.IOException;

import static xyz.luan.file2queue.PubSubQueue.provider;

public class Client {

    private String projectId;
    private String subscriptionId;

    public Client(String projectId, String subscriptionId) {
        this.projectId = projectId;
        this.subscriptionId = subscriptionId;
    }

    public void run() throws IOException {
        FlowControlSettings flowControlSettings = FlowControlSettings.newBuilder().setMaxOutstandingElementCount(2L).build();
        ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder().setExecutorThreadCount(1000).build();
        SubscriptionName subscriptionName = SubscriptionName.create(projectId, subscriptionId);
        MessageReceiver receiver = (message, consumer) -> {
            System.out.println("Id : " + message.getMessageId());
            System.out.println("Data : " + message.getData().toStringUtf8());
            System.out.println("Processing!");
            sleep(10000);
            System.out.println("Done!");
            consumer.ack();
        };

        String file = "/home/luan/projects/file2queue/.key.json";
        Subscriber subscriber = Subscriber.defaultBuilder(subscriptionName, receiver).setCredentialsProvider(provider(file)).setExecutorProvider(executorProvider).setFlowControlSettings(flowControlSettings).build();
        System.out.println("Starting!");
        subscriber.startAsync();
        sleep(50000);
        subscriber.startAsync();
        System.out.println("Ending!");
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
