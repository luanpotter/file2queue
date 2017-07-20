package xyz.luan.file2queue;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.FileInputStream;
import java.io.IOException;

public class PubSubQueue implements Queue {

    private final TopicAdminClient topicAdminClient;
    private final Publisher publisher;

    public PubSubQueue(String project, String topicId, String file) {
        TopicName name = TopicName.create(project, topicId);
        try {
            this.topicAdminClient = createTopicAdmin(file);
            this.publisher = createPublisher(name, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Publisher createPublisher(TopicName name, String file) throws IOException {
        return Publisher.defaultBuilder(name).setCredentialsProvider(provider(file)).build();
    }

    private TopicAdminClient createTopicAdmin(String file) throws IOException {
        CredentialsProvider provider = provider(file);
        TopicAdminSettings s = TopicAdminSettings.defaultBuilder().setCredentialsProvider(provider).build();
        return TopicAdminClient.create(s);
    }

    private CredentialsProvider provider(String file) throws IOException {
        return FixedCredentialsProvider.create(GoogleCredentials.fromStream(new FileInputStream(file)));
    }

    @Override
    public void send(String message) {
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        publisher.publish(pubsubMessage);
    }

    @Override
    public void close() {
        try {
            publisher.shutdown();
            topicAdminClient.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
