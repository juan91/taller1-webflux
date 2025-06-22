package org.example.handler;

import org.example.config.AMQPConfig;
import org.example.dto.TransactionDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

    private final AmqpTemplate amqpTemplate;

    public Publisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publish(TransactionDto transaction) {
        amqpTemplate.convertAndSend(AMQPConfig.EXCHANGE, AMQPConfig.ROUTING_KEY, transaction);
    }
}
