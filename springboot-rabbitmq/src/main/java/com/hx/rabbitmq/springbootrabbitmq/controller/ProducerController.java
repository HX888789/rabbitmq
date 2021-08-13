package com.hx.rabbitmq.springbootrabbitmq.controller;

import com.hx.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author huangxu
 */
@RestController
@Slf4j
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发消息
     */
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message){
        CorrelationData correlationData = new CorrelationData("1");
        log.info("当前时间：{}，发送了消息：{}",new Date(),message);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,ConfirmConfig.CONFIRM_KEY,message,correlationData);
    }
}
