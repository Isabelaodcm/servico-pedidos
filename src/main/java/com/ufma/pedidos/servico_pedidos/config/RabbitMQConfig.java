package com.ufma.pedidos.servico_pedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "pedido.exchange";

    public static final String QUEUE_COZINHA = "pedido.cozinha";
    public static final String QUEUE_ENTREGA = "pedido.entrega";

    public static final String ROUTING_KEY_COZINHA = "pedido.cozinha";
    public static final String ROUTING_KEY_ENTREGA = "pedido.entrega";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // ---------- COZINHA ----------
    @Bean
    public Queue cozinhaQueue() {
        return new Queue(QUEUE_COZINHA);
    }

    @Bean
    public Binding cozinhaBinding() {
        return BindingBuilder
                .bind(cozinhaQueue())
                .to(exchange())
                .with(ROUTING_KEY_COZINHA);
    }

    // ---------- ENTREGA ----------
    @Bean
    public Queue entregaQueue() {
        return new Queue(QUEUE_ENTREGA);
    }

    @Bean
    public Binding entregaBinding() {
        return BindingBuilder
                .bind(entregaQueue())
                .to(exchange())
                .with(ROUTING_KEY_ENTREGA);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
