package xyz.luan.file2queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsQueue implements Queue {

    private final Session session;
    private final MessageProducer producer;
    private final Connection connection;

    public JmsQueue(String brokerURL, String queue) {
        try {
            this.connection = createConnection(brokerURL);
            this.session = createSession();
            this.producer = createProducer(queue);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private Session createSession() throws JMSException {
        return this.connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
    }

    private Connection createConnection(String brokerURL) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private MessageProducer createProducer(String queue) throws JMSException {
        MessageProducer producer = session.createProducer(session.createQueue(queue));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return producer;
    }

    @Override
    public void send(String message) {
        try {
            producer.send(session.createTextMessage(message));
        } catch (JMSException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
