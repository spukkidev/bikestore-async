package com.example.bikestoreasync;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/ordenar")
    public String placeOrder() {
        Order order = new Order();
        order.setPedidoId(UUID.randomUUID().toString());
        order.setPaymentStatus("PENDING");

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ORDER_QUEUE, order);

        System.out.printf("[%s] [Thread: %s] FRONTEND: Pedido %s publicado en RabbitMQ.%n",
                Instant.now(), Thread.currentThread().getName(), order.getPedidoId());

        return "Pedido " + order.getPedidoId() + " enviado al broker.";
    }
}