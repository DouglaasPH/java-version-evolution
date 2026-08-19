package org.example.entities;

import java.math.BigDecimal;

public record ResultadoPedido(Pedido pedido, BigDecimal totalReais, BigDecimal totalDolar) {}
