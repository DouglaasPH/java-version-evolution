package org.example.entities;

import org.example.entities.enums.TipoCliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Pedido(int id, String nomeCliente, TipoCliente tipoCliente, List<ItemPedido> itens) {

    // Compact constructor: roda antes da atribuição automática dos campos.
    // Serve para validar ou, como aqui, normalizar um valor recebido.
    public Pedido {
        itens = Collections.unmodifiableList(new ArrayList<>(itens));
    }

    public BigDecimal calcularSubTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " (" + nomeCliente + ", " + tipoCliente + ")";
    }
}
