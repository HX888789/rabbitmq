package com.hx.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Ttl队列
 */
@Configuration
public class TtlQueueConfig {
    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列名称
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 声明 X交换机
     */
    @Bean
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明 Y交换机
     */
    @Bean
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue queueA(){

        Map<String,Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置私信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置ttl的过期时间 单位是ms
        arguments.put("x-message-ttl",1000 * 10);
        return  QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean
    public Queue queueB(){

        Map<String,Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置私信routingKey
        arguments.put("x-dead-letter-routing-key","YD");
        //设置ttl的过期时间 单位是ms
        arguments.put("x-message-ttl",1000 * 40);
        return  QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean
    public Queue queueD(){
        return  QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }
    //绑定
    @Bean
    public Binding queueABingDingX(Queue queueA, DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定
    @Bean
    public Binding queueBBingDingX(Queue queueB, DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定
    @Bean
    public Binding queueBBingDingY(Queue queueD, DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
