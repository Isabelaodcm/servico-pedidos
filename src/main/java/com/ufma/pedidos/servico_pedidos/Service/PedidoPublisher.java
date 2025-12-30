package com.ufma.pedidos.servico_pedidos.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.ufma.pedidos.servico_pedidos.Events.PedidoStatusEvent;
import com.ufma.pedidos.servico_pedidos.config.RabbitMQConfig;

@Service
public class PedidoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PedidoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarStatus(Long pedidoId, Long clienteId, String status) {
        PedidoStatusEvent event =
                new PedidoStatusEvent(pedidoId, clienteId, status);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}

