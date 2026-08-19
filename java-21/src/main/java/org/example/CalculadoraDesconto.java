package org.example;

import org.example.entities.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraDesconto {
    private CalculadoraDesconto() {
    }

    public static BigDecimal calcularTotalComDesconto(Pedido pedido) {
        BigDecimal subtotal = pedido.calcularSubTotal();

        int percentualDesconto = switch (pedido.tipoCliente()) {
            case NORMAL -> 0;
            case VIP -> 10;
            case ATACADO -> 20;
        };

        BigDecimal fatorDesconto = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(percentualDesconto).divide(BigDecimal.valueOf(100))
        );

        return subtotal.multiply(fatorDesconto).setScale(2, RoundingMode.HALF_UP);
    }
}
