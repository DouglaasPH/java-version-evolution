package org.example.entities;

import java.math.BigDecimal;

public record ItemPedido(Produto produto, int quantidade) {
    public BigDecimal getSubtotal() {
        return produto.preco().multiply(BigDecimal.valueOf(quantidade));
    }

    @Override
    public String toString() {
        return quantidade + "x " + produto.nome() + " (subtotal: R$ " + getSubtotal() + ")";
    }
}
