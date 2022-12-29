package rabibitmq.simplejavaclient;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Send {

    private final static String QUEUE_NAME = "hello";

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

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello world message";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }
}
