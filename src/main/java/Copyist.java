import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

public class Copyist {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    Logger log = Logger.getLogger(getClass());

    String queueProducer;
    String queueConsumer1;
    String queueConsumer2;

    Connection connection;

    public Copyist(String queueProducer, String queueConsumer1, String queueConsumer2) throws JMSException {

        this.queueProducer = queueProducer;
        this.queueConsumer1 = queueConsumer1;
        this.queueConsumer2 = queueConsumer2;

        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        connection = factory.createConnection();
        connection.start();

    }

    public Copyist(Connection connection, String queueProducer, String queueConsumer1, String queueConsumer2) throws JMSException {

        this.queueProducer = queueProducer;
        this.queueConsumer1 = queueConsumer1;
        this.queueConsumer2 = queueConsumer2;
        this.connection = connection;
        connection.start();

    }


    public void work() throws JMSException {

        Message message = receive(queueProducer);

        if (notEmpty(message)) {
            send(message, queueConsumer1);
            send(message, queueConsumer2);
        } else {
            log.info("Message is Empty! " + message);
            System.out.println("Message is Empty!");
        }

    }

    public boolean notEmpty(Message message) throws JMSException {
        return !(message instanceof TextMessage && ((TextMessage) message).getText().isEmpty());
    }


    public void send(Message message, String queue) throws JMSException {
        log.info("send " + queue);
        Session session = createSession();
        Destination destination = session.createQueue(queue);
        MessageProducer producer = session.createProducer(destination);
        producer.send(message);
        session.close();
    }


    public Message receive(String queue) throws JMSException {
        log.info("receive " + queue);
        Session session = createSession();
        Destination destination = session.createQueue(queue);
        MessageConsumer consumer = session.createConsumer(destination);
        Message message = consumer.receive();
        session.close();
        return message;
    }

    public Session createSession() throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void close() throws JMSException {
        connection.close();
    }
}
