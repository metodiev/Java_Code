package rabibitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import javafx.concurrent.Task;

import java.io.IOException;

public class NewTask {

    private final static String QUEUE_NAME = "hello";
    private final static String TASK_QUEUE = "task_queue";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");


        Channel channel = null;
        try{
            Connection connection = factory.newConnection();
            channel = connection.createChannel();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE, durable, false, false, null);
        String message = String.join(" ", args);
       // channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        channel.basicPublish("", TASK_QUEUE,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));
        System.out.println(" [x] Sent'" + message + "'");
    }
}
