package com.ufma.pedidos.servico_pedidos.Events;

public record PedidoStatusEvent(
        Long pedidoId,
        Long clienteId,
        String status
) {}

