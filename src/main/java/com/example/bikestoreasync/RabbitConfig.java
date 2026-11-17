package com.example.bikestoreasync;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_QUEUE = "bikestore.orders";
    public static final String EMAIL_QUEUE = "bikestore.emails";
    public static final String DLQ_QUEUE = "bikestore.dlq";
    public static final String EXCHANGE = "bikestore.exchange";

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue emailQueue() { return new Queue(EMAIL_QUEUE, true); }

    @Bean
    public Queue deadLetterQueue() { return new Queue(DLQ_QUEUE, true); }

    @Bean
    public DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with(ORDER_QUEUE);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(EMAIL_QUEUE);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}