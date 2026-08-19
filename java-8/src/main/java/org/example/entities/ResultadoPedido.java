package org.example.entities;

import java.math.BigDecimal;

public class ResultadoPedido {
    private final Pedido pedido;
    private final BigDecimal totalReais;
    private final BigDecimal totalDolar;

    public ResultadoPedido(Pedido pedido, BigDecimal totalReais, BigDecimal totalDolar) {
        this.pedido = pedido;
        this.totalReais = totalReais;
        this.totalDolar = totalDolar;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public BigDecimal getTotalReais() {
        return totalReais;
    }

    public BigDecimal getTotalDolar() {
        return totalDolar;
    }
}
