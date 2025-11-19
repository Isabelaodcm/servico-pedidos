package com.ufma.pedidos.servico_pedidos.DTO;

import com.ufma.pedidos.servico_pedidos.Model.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDto(
        Long id,
        Long clienteId,
        LocalDateTime dataHora,
        StatusPedido status,
        List<ItemResponseDTO> itens
) {
    public record ItemResponseDTO(Long id, String nome, int quantidade) {}
}

