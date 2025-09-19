package com.crane.answer.manager.mq;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author crane
 * @date 2025.09.16 下午4:08
 * @description
 **/
@Component
@Slf4j
public class MessageManger {
    @Resource
    private RabbitTemplate rabbitTemplate;


    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
