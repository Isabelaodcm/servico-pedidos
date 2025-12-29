package com.ufma.pedidos.servico_pedidos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ufma.pedidos.servico_pedidos.DTO.CozinhaPedidoRequest;
import com.ufma.pedidos.servico_pedidos.DTO.ItemPedidoDto;
import com.ufma.pedidos.servico_pedidos.Model.Pedido;

@Component
public class CozinhaClient {

    private final RestTemplate restTemplate;

    @Value("${cozinha.url}")
    private String cozinhaUrl;

    public CozinhaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void enviarPedido(Pedido pedido) {

        CozinhaPedidoRequest request = new CozinhaPedidoRequest(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getItens().stream()
                        .map(i -> new ItemPedidoDto(i.getNome(), i.getQuantidade()))
                        .toList()
        );

        restTemplate.postForEntity(
                cozinhaUrl + "/cozinha/pedidos",
                request,
                Void.class
        );
    }
    
    public void iniciarPreparo(Long pedidoId) {
        restTemplate.put(cozinhaUrl + "/cozinha/pedidos/" + pedidoId + "/iniciar", null);
    }

    public void finalizarPedido(Long pedidoId) {
        restTemplate.put(cozinhaUrl + "/cozinha/pedidos/" + pedidoId + "/finalizar", null);
    }
}
