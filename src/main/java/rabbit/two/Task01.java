package rabbit.two;

import com.rabbitmq.client.Channel;
import rabbit.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * 生产者 发送大量的消息
 */
public class Task01 {
    //队列的名称
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //1.交换机 2.路由的key值是什么 本次是队列名称 3. 其它参数信息 4.消息内容
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成 ：" + message);
        }

    }
}
