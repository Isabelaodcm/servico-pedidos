package com.ufma.pedidos.servico_pedidos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufma.pedidos.servico_pedidos.Model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
