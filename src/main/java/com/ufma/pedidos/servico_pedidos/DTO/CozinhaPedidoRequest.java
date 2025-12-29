package com.ufma.pedidos.servico_pedidos.DTO;

import java.util.List;

public record CozinhaPedidoRequest(         
		Long id, //id do pedido no schema de pedidos
        Long clienteId,
        List<ItemPedidoDto> itens) {

}
