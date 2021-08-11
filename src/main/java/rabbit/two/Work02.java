package rabbit.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import rabbit.util.RabbitMqUtils;

import java.util.Arrays;

/**
 * 这是一个工作线程
 * @author huangxu
 */
public class Work02 {

    //队列的名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //消息的接收
        //2.是否自动应答
        DeliverCallback deliverCallback = (t1,t2) -> System.out.println(new String(t2.getBody()));
        CancelCallback cancelCallback = t1 -> System.out.println(t1 + "消费者取消消费接口回调逻辑");
        System.out.println("第二个工作线程，等待接收消息......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }

}
