package com.ufma.pedidos.servico_pedidos.Service;

import com.ufma.pedidos.servico_pedidos.DTO.*;
import com.ufma.pedidos.servico_pedidos.Model.*;
import com.ufma.pedidos.servico_pedidos.Repository.PedidoRepository;
import com.ufma.pedidos.servico_pedidos.Events.MensageriaPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final MensageriaPublisher publisher;

    public PedidoService(PedidoRepository repository, MensageriaPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public PedidoResponseDto criarPedido(PedidoRequestDto dto) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.clienteId());
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.RECEBIDO);

        var itens = dto.itens().stream().map(item ->
                new ItemPedido(null, item.nome(), item.quantidade())
        ).collect(Collectors.toList());

        pedido.setItens(itens);

        Pedido salvo = repository.save(pedido);

        publisher.publicar("pedidos.recebidos", salvo);

        return toDTO(salvo);
    }

    public PedidoResponseDto buscar(long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public PedidoResponseDto atualizarStatus(long id, AtualizarStatusDto dto) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(dto.status());
        repository.save(pedido);

        publisher.publicar("pedidos.status", pedido);

        return toDTO(pedido);
    }

    public PedidoResponseDto finalizar(long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.FINALIZADO);
        repository.save(pedido);

        publisher.publicar("pedidos.finalizados", pedido);

        return toDTO(pedido);
    }

    private PedidoResponseDto toDTO(Pedido p) {
        return new PedidoResponseDto(
                p.getId(),
                p.getClienteId(),
                p.getDataHora(),
                p.getStatus(),
                p.getItens().stream().map(i ->
                        new PedidoResponseDto.ItemResponseDTO(i.getId(), i.getNome(), i.getQuantidade())
                ).collect(Collectors.toList())
        );
    }
}

