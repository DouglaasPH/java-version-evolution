package org.example;

import org.example.entities.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Aplica o desconto de acordo com o tipo de cliente do pedido.
 *
 * Em Java 21+ dá para escrever isso com pattern matching de switch,
 * de forma mais compacta. Aqui, em Java 8, usamos um switch tradicional
 * sobre o enum — funciona igual, só é um pouco mais verboso.
 */
public class CalculadoraDesconto {
    private CalculadoraDesconto() {
        // classe utilitária: não deve ser instanciada
    }

    public static BigDecimal calcularTotalComDesconto(Pedido pedido) {
        BigDecimal subtotal = pedido.calcularSubtotal();
        int percentualDesconto;

        switch (pedido.getTipoCliente()) {
            case NORMAL:
                percentualDesconto = 0;
                break;
            case VIP:
                percentualDesconto = 10;
                break;
            case ATACADO:
                percentualDesconto = 20;
                break;
            default:
                throw new IllegalStateException("Tipo de cliente não mapeado: " + pedido.getTipoCliente());
        }

        BigDecimal fatorDesconto = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(percentualDesconto).divide(BigDecimal.valueOf(100))
        );

        return subtotal.multiply(fatorDesconto).setScale(2, RoundingMode.HALF_UP);
    }
}
