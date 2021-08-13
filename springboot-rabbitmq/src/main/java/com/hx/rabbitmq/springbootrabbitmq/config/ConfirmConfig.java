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
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE = "backup_queue";
    /**
     * 报警队列
     */
    public static final String WARING_QUEUE = "warning_queue";


    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.
                directExchange(CONFIRM_EXCHANGE).
                withArgument("alternate-exchange",BACKUP_EXCHANGE).
                durable(true).build();
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

    /**
     * 备份队列
     * @return 备份队列
     */
    @Bean
    public Queue backupQueue(){
        return new Queue(BACKUP_QUEUE);
    }
    /**
     * 警告队列
     */
    @Bean
    public Queue warningQueue(){
        return new Queue(WARING_QUEUE);
    }
    /**
     * 订阅发布交换机
     */
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }
    /**
     * 绑定
     */
    @Bean
    public Binding warningQueueBindingBackupExchange(Queue warningQueue,FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(Queue backupQueue,FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
}
