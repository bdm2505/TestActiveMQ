
import javax.jms.JMSException;

public class Start {


    public static void main(String[] args) throws JMSException {
        Copyist copyist = new Copyist("Queue A", "Queue B", "Queue A");

        while (!Thread.currentThread().isInterrupted()) {
            copyist.work();
        }
        copyist.close();
    }
}
