import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Postman {
    final String url;
    final ConnectionFactory connectionFactory;
    Connection connection;
    final String queueName = "My Queue";


    public Postman(String url) throws JMSException {
        this.url = url;
        connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
    }


    public void send(Message message) throws JMSException {
        Session session = createSession();
        Queue queue = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(queue);
        producer.send(message);
        session.commit();
        session.close();
    }


    public Message receive() throws JMSException {
        Session session = createSession();
        Queue queue = session.createQueue(queueName);
        MessageConsumer consumer = session.createConsumer(queue);
        Message message = consumer.receive();
        session.close();
        return message;
    }

    private Session createSession() throws JMSException {
        return connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
    }

    public void close() {
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
