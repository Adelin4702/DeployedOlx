package com.olxapplication.config;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.dtos.UserMailDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());

    public void send(UserMailDTO userDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, userDto);
        logger.info("Sending UserDto to the Queue : " + userDto.toString());
    }
}
