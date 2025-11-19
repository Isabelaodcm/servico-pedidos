package com.ufma.pedidos.servico_pedidos.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    private Long clienteId;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}

