package org.example;

import org.example.entities.ItemPedido;
import org.example.entities.Pedido;
import org.example.entities.Produto;
import org.example.entities.ResultadoPedido;
import org.example.entities.enums.TipoCliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ponto de entrada do programa.
 *
 * Repare que em Java 8 o main precisa ser exatamente assim:
 * "public static void main(String[] args)" dentro de uma classe pública.
 * Em Java 25 dá pra escrever só "void main()" sem classe nenhuma — mas
 * isso não existe em nenhuma versão anterior à 25.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ---- Catálogo de produtos ----
        Produto notebook = new Produto("Notebook", new BigDecimal("3500.00"));
        Produto mouse = new Produto("Mouse sem fio", new BigDecimal("80.00"));
        Produto monitor = new Produto("Monitor 24\"", new BigDecimal("900.00"));
        Produto teclado = new Produto("Teclado mecânico", new BigDecimal("350.00"));

        // ---- Pedidos de exemplo ----
        List<Pedido> pedidos = new ArrayList<>();

        pedidos.add(new Pedido(1, "Ana Silva", TipoCliente.NORMAL, Arrays.asList(
                new ItemPedido(notebook, 1),
                new ItemPedido(mouse, 1)
        )));

        pedidos.add(new Pedido(2, "Bruno Costa", TipoCliente.VIP, Arrays.asList(
                new ItemPedido(monitor, 2),
                new ItemPedido(teclado, 1)
        )));

        pedidos.add(new Pedido(3, "Distribuidora XYZ", TipoCliente.ATACADO, Arrays.asList(
                new ItemPedido(mouse, 50),
                new ItemPedido(teclado, 20)
        )));

        pedidos.add(new Pedido(4, "Carla Souza", TipoCliente.NORMAL, Arrays.asList(
                new ItemPedido(monitor, 1)
        )));

        // ---- Processamento ----
        System.out.println("Processando " + pedidos.size() + " pedido(s)...\n");

        long inicio = System.currentTimeMillis();

        ProcessadorPedidos processador = new ProcessadorPedidos(new CotacaoService());
        List<ResultadoPedido> resultados = processador.processar(pedidos);

        long duracaoMs = System.currentTimeMillis() - inicio;

        // ---- Relatório final ----
        System.out.println();
        System.out.println(GeradorRelatorio.gerar(resultados));
        System.out.println("Tempo total de processamento: " + duracaoMs + "ms");
    }
}