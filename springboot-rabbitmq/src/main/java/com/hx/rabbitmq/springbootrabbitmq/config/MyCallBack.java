package com.hx.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    /*注入*/
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }


    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{}，被交换机{}，回退了，退回的原因：{}，路由key：{}",new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        //交换机确认回调方法 1.发消息 交换机接收到了 回调 correlationData保存消息的ID及相关信息 b 是成功还是失败 s失败原因
        String id = correlationData != null ? correlationData.getId() : "";
        if(b){
            log.info("交换机已经收到了id为{}的消息",id);
        }  else {
            log.info("交换机还没有收到id为{}的消息 原因是{}",id,s);
        }
    }
}
