package com.example.bikestoreasync;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WorkersService {

    private final RabbitTemplate rabbitTemplate;

    public WorkersService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void processPayment(Order order) {
        System.out.printf("[%s] [Thread: %s] PaymentWorker: Procesando cobro para %s...%n",
                Instant.now(), Thread.currentThread().getName(), order.getPedidoId());

        if (Math.random() < 0.5) {
            System.err.printf("[%s] [Thread: %s] ERROR: Fallo de tarjeta para %s. Reintentando...%n",
                    Instant.now(), Thread.currentThread().getName(), order.getPedidoId());
            throw new RuntimeException("Error simulado en pasarela de pago");
        }

        order.setPaymentStatus("PAID");
        System.out.printf("[%s] [Thread: %s] PAGO APROBADO: %s. Notificando a EmailWorker.%n",
                Instant.now(), Thread.currentThread().getName(), order.getPedidoId());

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.EMAIL_QUEUE, order);
    }

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void sendEmail(Order order) {
        if ("PAID".equals(order.getPaymentStatus())) {
            System.out.printf("[%s] [Thread: %s] EmailWorker: Enviando correo de confirmación para pedido %s.%n",
                    Instant.now(), Thread.currentThread().getName(), order.getPedidoId());
        }
    }
}