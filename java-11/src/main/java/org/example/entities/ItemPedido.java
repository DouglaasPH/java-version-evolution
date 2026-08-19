package org.example.entities;

import java.math.BigDecimal;

public class ItemPedido {
    private final Produto produto;
    private final int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSubtotal() { return produto.getPreco().multiply(BigDecimal.valueOf(quantidade)); }

    @Override
    public String toString() {
        return quantidade + "x " + produto.getNome() + " (subtotal: R$ " + getSubtotal() + ")";
    }
}
