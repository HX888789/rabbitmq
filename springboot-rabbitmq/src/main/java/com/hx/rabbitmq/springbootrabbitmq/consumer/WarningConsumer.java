package com.hx.rabbitmq.springbootrabbitmq.consumer;

import com.hx.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARING_QUEUE)
    public void warningConsumer(Message message){
        String s = new String(message.getBody());
        log.info("报警发现不可路由的消息，{}",s);
    }
}
