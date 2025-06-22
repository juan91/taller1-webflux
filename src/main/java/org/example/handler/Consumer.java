package org.example.handler;

import org.example.dto.TransactionDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = "transaction.created.queue")
    public void handler(TransactionDto transaction) {
        System.out.println("evento recibo "+transaction.toString());
    }

}
