package com.ufma.pedidos.servico_pedidos.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ufma.pedidos.servico_pedidos.DTO.AtualizarStatusDto;
import com.ufma.pedidos.servico_pedidos.DTO.PedidoRequestDto;
import com.ufma.pedidos.servico_pedidos.DTO.PedidoResponseDto;
import com.ufma.pedidos.servico_pedidos.Events.MensageriaPublisher;
import com.ufma.pedidos.servico_pedidos.Model.ItemPedido;
import com.ufma.pedidos.servico_pedidos.Model.Pedido;
import com.ufma.pedidos.servico_pedidos.Model.StatusPedido;
import com.ufma.pedidos.servico_pedidos.Repository.PedidoRepository;
import com.ufma.pedidos.servico_pedidos.client.CozinhaClient;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final MensageriaPublisher publisher;
    
    private final CozinhaClient cozinhaClient;
    
    private final PedidoPublisher pedidoPublisher;

//    public PedidoService(PedidoRepository repository, MensageriaPublisher publisher) {
//        this.repository = repository;
//        this.publisher = publisher;
//    }
    
    public PedidoService(PedidoRepository repository, MensageriaPublisher publisher,  CozinhaClient cozinhaClient, PedidoPublisher pedidoPublisher) {
		this.repository = repository;
		this.publisher = publisher;
		this.cozinhaClient = cozinhaClient;
		this.pedidoPublisher = pedidoPublisher;
		}

    public PedidoResponseDto criarPedido(PedidoRequestDto dto) {

        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.clienteId());
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.RECEBIDO);
//        pedido.setStatus(StatusPedido.EM_PREPARO);

        dto.itens().forEach(itemDto -> {
            ItemPedido item = new ItemPedido();
            item.setNome(itemDto.nome());
            item.setQuantidade(itemDto.quantidade());
            pedido.addItem(item); 
        });

        Pedido salvo = repository.save(pedido);

        System.out.println("ID DO PEDIDO: " + salvo.getId());
        // integração com cozinha
//        cozinhaClient.enviarPedido(salvo);


        pedidoPublisher.enviarStatus(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getStatus().name()
        );

        return toDTO(salvo);
    }



    public PedidoResponseDto buscar(long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

//    public PedidoResponseDto atualizarStatus(long id, AtualizarStatusDto dto) {
//        Pedido pedido = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
//
//        pedido.setStatus(dto.status());
//        repository.save(pedido);
//
//        publisher.publicar("pedidos.status", pedido);
//
//        return toDTO(pedido);
//    }

//    public PedidoResponseDto finalizar(long id) {
//        Pedido pedido = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
//
//        pedido.setStatus(StatusPedido.FINALIZADO);
//        repository.save(pedido);
//
//        publisher.publicar("pedidos.finalizados", pedido);
//
//        return toDTO(pedido);
//    }
    
    @Transactional
    public PedidoResponseDto finalizarPreparo(Long pedidoId) {

        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.EM_PREPARO) {
            throw new RuntimeException("Pedido não está em preparo");
        }

//        cozinhaClient.finalizarPedido(pedidoId);

        pedido.setStatus(StatusPedido.PRONTO);
        repository.save(pedido);
        
        pedidoPublisher.enviarStatus(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getStatus().name()
        );

        return toDTO(pedido);
    }

    
    @Transactional
    public PedidoResponseDto iniciarPreparo(Long pedidoId) {

        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.RECEBIDO) {
            throw new RuntimeException("Pedido não pode iniciar preparo");
        }

//        cozinhaClient.iniciarPreparo(pedidoId);

        pedido.setStatus(StatusPedido.EM_PREPARO);
        repository.save(pedido);
        
        pedidoPublisher.enviarStatus(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getStatus().name()
        );

        return toDTO(pedido);
    }


    @Transactional
    public PedidoResponseDto aguardarRetirada(Long pedidoId) {

        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.PRONTO) {
            throw new RuntimeException("Pedido ainda não está pronto para retirada");
        }

        pedido.setStatus(StatusPedido.AGUARDANDO_RETIRADA);
        repository.save(pedido);

        pedidoPublisher.enviarStatus(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getStatus().name()
        );

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

