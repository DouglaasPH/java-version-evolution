package org.example;

import org.example.entities.ItemPedido;
import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;

import java.math.BigDecimal;
import java.util.List;

public final class GeradorRelatorio {
    private static final String CABECALHO = """
            ========================================
             RELATORIO DE PEDIDOS PROCESSADOS
            ========================================

            """;

    private GeradorRelatorio() {
    }

    public static String gerar(List<ResultadoPedido> resultados) {
        StringBuilder sb = new StringBuilder();
        BigDecimal totalGeralReais = BigDecimal.ZERO;

        for (ResultadoPedido resultado : resultados) {
            Pedido pedido = resultado.pedido();
            sb.append("Pedido #").append(pedido.id()).append("\n");
            sb.append("  Cliente : ").append(pedido.nomeCliente())
                    .append(" (").append(pedido.tipoCliente()).append(")\n");

            for (ItemPedido item : pedido.itens()) {
                sb.append("  - ").append(item).append("\n");
            }

            sb.append("  Total   : R$ ").append(resultado.totalReais())
                    .append("  (US$ ").append(resultado.totalDolar()).append(")\n\n");

            totalGeralReais = totalGeralReais.add(resultado.totalReais());
        }

        sb.append("----------------------------------------\n");
        sb.append("TOTAL GERAL: R$ ").append(totalGeralReais).append("\n");
        sb.append("========================================\n");

        return sb.toString();
    }
}
