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
    public static final String ROUTING_KEY = "pedido.cozinha";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue cozinhaQueue() {
        return new Queue(QUEUE_COZINHA);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(cozinhaQueue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
