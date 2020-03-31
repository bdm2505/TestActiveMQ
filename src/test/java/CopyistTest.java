import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.*;

public class CopyistTest {

    final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
            "vm://localhost?broker.persistent=false");


    @Test
    public void testNotEmpty() throws JMSException {
        Copyist copyist = new Copyist(connectionFactory.createConnection(), "Q1", "Q2", "Q3");
        Session session = copyist.createSession();

        Message notEmptyMessage = session.createTextMessage("hello world");
        Message emptyMessage = session.createTextMessage("");

        assertTrue(copyist.notEmpty(notEmptyMessage));
        assertFalse(copyist.notEmpty(emptyMessage));

        session.close();
    }

    @Test
    public void testWork() throws JMSException {
        Copyist copyist = new Copyist(connectionFactory.createConnection(), "Q1", "Q2", "Q3");
        Session session = copyist.createSession();

        copyist.send(session.createTextMessage("text"), "Q1");
        copyist.work();

        String message1 = ((TextMessage) copyist.receive("Q2")).getText();
        String message2 = ((TextMessage) copyist.receive("Q3")).getText();

        assertEquals(message1, "text");
        assertEquals(message2, "text");
    }
}
