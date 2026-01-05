# Sistema de Pedidos para Restaurante

## Objetivo
Sistema distribuído para simular o fluxo de pedidos de um restaurante,
utilizando microserviços, APIs REST e comunicação assíncrona via eventos.

## Serviços
- Pedido Service (Java / Spring Boot)
- Cozinha Service (Node.js)
- Entrega Service (Node.js)

## Infraestrutura
- PostgreSQL
- RabbitMQ
- Docker

## Arquitetura
- Comunicação REST (front-end → serviços)
- Comunicação assíncrona (RabbitMQ entre serviços)
