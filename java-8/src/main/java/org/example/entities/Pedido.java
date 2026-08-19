package org.example.entities;

import org.example.entities.enums.TipoCliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final String nomeCliente;
    private final TipoCliente tipoCliente;
    private final List<ItemPedido> itens;

    public Pedido(int id, String nomeCliente, TipoCliente tipoCliente, List<ItemPedido> itens) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.tipoCliente = tipoCliente;
        this.itens = Collections.unmodifiableList(new ArrayList<>(itens));
    }

    public int getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public BigDecimal calcularSubtotal() {
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
