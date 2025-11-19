package com.ufma.pedidos.servico_pedidos.DTO;

import java.util.List;

public record PedidoRequestDto(
        Long clienteId,
        List<ItemDTO> itens
) {
    public record ItemDTO(String nome, int quantidade) {}
}

