package com.hx.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列插件
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 队列
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    /**
     * 交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    /**
     * 交换机
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    /**
     * 声明交换机 基于插件的交换机
     */
    @Bean
    public CustomExchange deleyedExChange(){
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type","direct");
        //1.交换机的名称 2.交换机的类型 3.是否持久化 4.是否自动删除 5.其他参数
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }

    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange(Queue delayedQueue,CustomExchange deleyedExChange){

        return BindingBuilder.bind(delayedQueue).to(deleyedExChange).with(DELAYED_ROUTING_KEY).noargs();

    }

}
