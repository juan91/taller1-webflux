package org.example.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

    // esto es para crear configuraciones desde codigo java(crear exchenge, routing etc), solo cuando hay permiso desde el server
    public static final String EXCHANGE = "transaction.galeras";
    public static final String ROUTING_KEY = "transaction.created";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue("transaction.created.queue", true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf){
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(messageConverter());
        template.setReplyTimeout(5000);
        template.setUseTemporaryReplyQueues(true);
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory cf){
        return new RabbitAdmin(cf);
    }



}
