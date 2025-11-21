package com.ufma.pedidos.servico_pedidos.Controller;

import com.ufma.pedidos.servico_pedidos.DTO.*;
import com.ufma.pedidos.servico_pedidos.Service.PedidoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public PedidoResponseDto criar(@RequestBody PedidoRequestDto dto) {
        return service.criarPedido(dto);
    }

    @GetMapping("/{id}")
    public PedidoResponseDto buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PatchMapping("/{id}/status")
    public PedidoResponseDto atualizarStatus(@PathVariable long id,
                                             @RequestBody AtualizarStatusDto dto) {
        return service.atualizarStatus(id, dto);
    }

    @PostMapping("/{id}/finalizar")
    public PedidoResponseDto finalizar(@PathVariable long id) {
        return service.finalizar(id);
    }
}

