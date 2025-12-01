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

        // Adiciona os itens corretamente
        for (var itemDto : dto.itens()) {
            ItemPedido item = new ItemPedido();
            item.setNome(itemDto.nome());
            item.setQuantidade(itemDto.quantidade());

            // RELACIONAMENTO CORRETO
            item.setPedido(pedido);
            pedido.getItens().add(item);
        }

        // Agora o Hibernate vai salvar pedido e itens com pedido_id corretamente
        Pedido salvo = repository.save(pedido);

        // Publica o evento
        publisher.publicar("pedidos.recebidos", salvo);

        return toDTO(salvo);
    }

//    private PedidoResponseDto toDTO(Pedido pedido) {
//        return new PedidoResponseDto(
//                pedido.getId(),
//                pedido.getClienteId(),
//                pedido.getStatus(),
//                pedido.getDataHora(),
//                pedido.getItens().stream().map(i ->
//                        new ItemPedidoResponseDto(
//                                i.getId(),
//                                i.getNome(),
//                                i.getQuantidade()
//                        )
//                ).toList()
//        );
//    }

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

