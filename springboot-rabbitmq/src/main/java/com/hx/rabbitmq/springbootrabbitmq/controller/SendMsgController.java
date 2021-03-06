package com.hx.rabbitmq.springbootrabbitmq.controller;

import com.hx.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 发送延迟消息
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 开始发消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message){
        log.info("当前时间：{}，发送一条信息给两个TTL队列:{}", new Date(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列"+message);

    }
    /**
     * 发设置ttl的消息
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable("message") String message,@PathVariable("ttlTime") String ttlTime){

        log.info("当前时间:{},发送一条市场为{}毫秒ttl信息给队列QC：{}",
                new Date(),ttlTime,message);
        rabbitTemplate.convertAndSend("x","XC",message,
                t -> {
                //发送消息时 这是延迟
                t.getMessageProperties().setExpiration(ttlTime);
                return t;}
               );
    }
    /**
     * 发送消息 基于插件的 消息及时间
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,@PathVariable("delayTime") Integer delayTime){
        log.info("当前时间:{},发送一条市场为{}毫秒ttl信息给队列QC：{}",
                new Date(),delayTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY +
                        message,t ->{
            t.getMessageProperties().setDelay(delayTime);
            return t;
        }
        );
    }
}
