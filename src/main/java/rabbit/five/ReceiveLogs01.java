package rabbit.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import rabbit.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * 消息的接收
 */
public class ReceiveLogs01 {
    //交换机的名称
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args)throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机 发布订阅模式
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列 临时队列,队列名称是随机的
        //当消费者断开与队列的连接的时候，队列就自动删除
        String queue = channel.queueDeclare().getQueue();
        /*
         * 绑定交换机与队列
         */
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("C1等待接收消息，把接收到的消息打印在屏幕上。。。。。。");
        DeliverCallback deliverCallback = (t1,t2) -> System.out.println(new String(t2.getBody(), StandardCharsets.UTF_8));

        channel.basicConsume(queue,true,deliverCallback,t1 -> System.out.println(t1 + "消息取消"));
    }
}
