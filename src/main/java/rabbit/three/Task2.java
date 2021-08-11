package rabbit.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import rabbit.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * 消息在手动应答的时候是不丢失的，放回队列中重新消费
 */
public class Task2 {
    //队列的名称
    public static final String QUEUE_NAME = "ack_queue";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //开启队列持久化 是否能共享消息 自动删除
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //从控制台中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //1.交换机 2.路由的key值是什么 本次是队列名称 3. 其它参数信息 4.消息内容
            //开启消息持久化,持久化到磁盘中
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("发送消息完成 ：" + message);
        }
    }
}
