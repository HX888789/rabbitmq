package rabbit.five;

import com.rabbitmq.client.Channel;
import rabbit.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * 负责进行发消息 交换机
 */
public class EmitLog {
    //交换机的名称
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args)throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println("生产者发出消息");
        }
    }
}
