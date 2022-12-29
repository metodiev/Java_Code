package rabibitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.MessageProperties;


public class Worker {

    private final static String QUEUE_NAME = "hello";
    private final static String TASK_QUEUE = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        boolean durable = true; //When RabbitMQ quits or crashes it will forget the queues and messages unless you tell it not
        // to. Two things are required to make sure that messages aren't lost: we need to mark both the queue and messages as durable.

        int prefetchCount = 1;
        channel.basicQos(prefetchCount); //accept only one unack-ed message at a time (see below)

        channel.queueDeclare(TASK_QUEUE, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Recived '" + message + "'");

            try {
                doWork(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("[x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        //boolean autoAck = true; //acknowLedgement is covered below
        channel.basicConsume(TASK_QUEUE,false, deliverCallback, consumerTag -> {});
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
