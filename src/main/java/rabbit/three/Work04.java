package rabbit.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import rabbit.util.RabbitMqUtils;

/**
 * 消息在手动应答的时候是不丢失的，放回队列中重新消费
 */
public class Work04 {
        public static final String QUEUE_NAME = "ack_queue";
        public static void main(String[] args) throws Exception{
            Channel channel = RabbitMqUtils.getChannel();
            System.out.println("C2接收消息处理时间较长");
            DeliverCallback deliverCallback = (t1,t2) -> {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new String(t2.getBody()));
                //手动应答
                /**
                 * 1.消息的标记 tag 2.是否批量应答 true:表示批量处理 false：表示不批量
                 */
                channel.basicAck(t2.getEnvelope().getDeliveryTag(),false);
            };
            //采用不公平分发 0为轮询方式 1为采用不公平分发，同样是预取值的写法
            channel.basicQos(1);

            //采用手动应答
            channel.basicConsume(QUEUE_NAME,false,deliverCallback,t -> System.out.println(t + "消费者取消消费 消费这借口回调"));
        }
}
