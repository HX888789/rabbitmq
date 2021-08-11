package rabbit.one;

import com.rabbitmq.client.*;

public class Consumer {
    private static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //成功的回调函数
        DeliverCallback deliverCallback = (var1,var2) -> System.out.println(new String(var2.getBody()));
        //失败的回调函数
        CancelCallback cancelCallback = var1 -> System.out.println("消息接收失败");

        //是否自动接收
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
