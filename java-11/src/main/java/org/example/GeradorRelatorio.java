package org.example;

import org.example.entities.ItemPedido;
import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;

import java.math.BigDecimal;
import java.util.List;

/**
 * Monta o relatório final em texto.
 *
 * Em Java 8 não existe Text Block, então strings de várias linhas
 * são montadas concatenando com "+" ou, como aqui, usando StringBuilder
 * — mais verboso do que a sintaxe """ ... """ do Java 17+.
 */
public final class GeradorRelatorio {
    private GeradorRelatorio() {
    }

    public static String gerar(List<ResultadoPedido> resultados) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append(" RELATORIO DE PEDIDOS PROCESSADOS\n");
        sb.append("========================================\n\n");

        BigDecimal totalGeralReais = BigDecimal.ZERO;

        for (ResultadoPedido resultado : resultados) {
            Pedido pedido = resultado.getPedido();
            sb.append("Pedido #").append(pedido.getId()).append("\n");
            sb.append("  Cliente : ").append(pedido.getNomeCliente())
                    .append(" (").append(pedido.getTipoCliente()).append(")\n");

            for (ItemPedido item : pedido.getItens()) {
                sb.append("  - ").append(item).append("\n");
            }

            sb.append("  Total   : R$ ").append(resultado.getTotalReais())
                    .append("  (US$ ").append(resultado.getTotalDolar()).append(")\n\n");

            totalGeralReais = totalGeralReais.add(resultado.getTotalReais());
        }

        sb.append("----------------------------------------\n");
        sb.append("TOTAL GERAL: R$ ").append(totalGeralReais).append("\n");
        sb.append("========================================\n");

        return sb.toString();
    }
}
