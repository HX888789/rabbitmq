package com.hx.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangxu
 */
@Configuration
/**
 * 发布确认防止消息丢失（放入缓存之中）
 */
public class ConfirmConfig {
    /**
     * 交换机
     */
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    /**
     * 队列
     */
    public static final String CONFIRM_QUEUE = "confirm_queue";
    /**
     * routingKey
     */
    public static final String CONFIRM_KEY = "key1";

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue confirmQueue(){
        return new Queue(CONFIRM_QUEUE);
    }
    /**
     * 绑定
     */
    @Bean
    public Binding confirmKeyBindingConfirmQueue(Queue confirmQueue,DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_KEY);
    }

}
