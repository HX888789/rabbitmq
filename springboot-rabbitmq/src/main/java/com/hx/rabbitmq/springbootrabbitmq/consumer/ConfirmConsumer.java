package com.hx.rabbitmq.springbootrabbitmq.consumer;

import com.hx.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void confirmConsumer(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，接收到了消息：{}",new Date(),msg);
    }
}
