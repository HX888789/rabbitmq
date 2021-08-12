package com.hx.rabbitmq.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 队列ttl 消费者
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    /**
     * @param message 消息
     * @param channel 信道
     * @throws Exception 异常
     */
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列消息：{}",new Date().toString(),msg);
    }

}
