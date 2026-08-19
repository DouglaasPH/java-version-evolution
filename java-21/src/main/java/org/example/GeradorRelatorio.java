package org.example;

import org.example.entities.ItemPedido;
import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;
import org.example.entities.enums.TipoCliente;

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
        StringBuilder sb = new StringBuilder(CABECALHO);
        BigDecimal totalGeralReais = BigDecimal.ZERO;

        for (ResultadoPedido resultado : resultados) {
            sb.append("Pedido #").append(identificarPedido(resultado.pedido())).append("\n");

            Pedido pedido = resultado.pedido();
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

        if (!resultados.isEmpty()) {
            sb.append("Primeiro pedido processado: #").append(resultados.getFirst().pedido().id()).append("\n");
            sb.append("Último pedido processado:   #").append(resultados.getLast().pedido().id()).append("\n");
        }

        sb.append("========================================\n");
        return sb.toString();
    }

    /**
     * Desestrutura o record Pedido diretamente na condição do "if"
     * (Record Patterns, JEP 440) para marcar pedidos de atacado.
     * Repare que "itens" fica disponível como variável já dentro do if,
     * sem precisar chamar pedido.itens() de novo.
     */
    private static String identificarPedido(Pedido pedidoObj) {
        if (pedidoObj instanceof Pedido(int id, String nome, TipoCliente tipo, var itens)
                && tipo == TipoCliente.ATACADO) {
            return id + " ⭐ (atacado, " + itens.size() + " itens)";
        }
        return String.valueOf(pedidoObj.id());
    }
}

