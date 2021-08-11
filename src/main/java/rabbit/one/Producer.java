package rabbit.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 1.队列名称
         * 2.是否开启持久化
         * 3。消息是否可以分享
         * 4.是否自动删除
         * 5.信息
         */
        channel.queueDeclare(QUEUE_NAME,false, false, false, null);
        channel.basicPublish("",QUEUE_NAME,null,"hello".getBytes());
        System.out.println("消息发送完毕");

    }
}
