import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class Copyist {
    Logger log = Logger.getLogger(getClass());

    final Postman producer;
    final Postman consumer1;
    final Postman consumer2;

    public Copyist(String urlProducer, String urlConsumer1, String urlConsumer2) throws JMSException {
        producer = new Postman(urlProducer);
        consumer1 = new Postman(urlConsumer1);
        consumer2 = new Postman(urlConsumer2);
    }


    public void work() throws JMSException {
        while (!Thread.currentThread().isInterrupted()) {
            Message message = producer.receive();

            if (notEmpty(message)) {
                consumer1.send(message);
                consumer2.send(message);
            } else {
                log.warn("Message is Empty!");
            }
        }
        close();
    }

    private boolean notEmpty(Message message) throws JMSException {
        return message instanceof TextMessage && !((TextMessage) message).getText().isEmpty();
    }

    private void close() {
        producer.close();
        consumer1.close();
        consumer2.close();
    }
}
