package org.example;

import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Processa vários pedidos "ao mesmo tempo".
 *
 * Em Java 8 a ferramenta disponível para concorrência é o pool de
 * threads tradicional (ExecutorService). Cada pedido processado aqui
 * simula uma chamada de I/O (por exemplo, uma verificação de estoque
 * num serviço externo) com um pequeno atraso artificial via
 * Thread.sleep — assim dá pra sentir, no relógio, a diferença entre
 * processar em sequência e processar com um pool.
 *
 * Quando este mesmo projeto for refeito em Java 21, o pool fixo abaixo
 * vira Executors.newVirtualThreadPerTaskExecutor() — o resto do código
 * praticamente não muda.
 */
public class ProcessadorPedidos {
    private static final int TAMANHO_POOL = 4;
    private static final long LATENCIA_SIMULADA_MS = 500;

    private final CotacaoService cotacaoService;

    public ProcessadorPedidos(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    public List<ResultadoPedido> processar(List<Pedido> pedidos) throws InterruptedException {
        // Busca a cotação uma única vez, antes de processar os pedidos.
        BigDecimal cotacaoDolar = cotacaoService.buscarCotacaoDolar();
        System.out.println("Cotação do dólar obtida: R$ " + cotacaoDolar);

        ExecutorService pool = Executors.newFixedThreadPool(TAMANHO_POOL);
        List<Future<ResultadoPedido>> futuros = new ArrayList<>();

        try {
            for (Pedido pedido : pedidos) {
                Callable<ResultadoPedido> tarefa = criarTarefa(pedido, cotacaoDolar);
                futuros.add(pool.submit(tarefa));
            }

            List<ResultadoPedido> resultados = new ArrayList<>();
            for (Future<ResultadoPedido> futuro : futuros) {
                resultados.add(obterResultado(futuro));
            }
            return resultados;

        } finally {
            pool.shutdown();
            pool.awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private Callable<ResultadoPedido> criarTarefa(Pedido pedido, BigDecimal cotacaoDolar) {
        // Lambda: a grande novidade do Java 8. Callable<ResultadoPedido> é uma
        // interface funcional (um único método abstrato, "call"), então dá
        // para implementá-la com uma expressão lambda em vez de uma classe
        // anônima — bem mais enxuto do que o Java exigia até a versão 7.
        return () -> {
            // Simula uma chamada de I/O (ex: checar estoque, validar cliente).
            Thread.sleep(LATENCIA_SIMULADA_MS);

            BigDecimal totalReais = CalculadoraDesconto.calcularTotalComDesconto(pedido);
            BigDecimal totalDolar = totalReais.divide(cotacaoDolar, 2, RoundingMode.HALF_UP);

            System.out.println("Pedido #" + pedido.getId() + " processado pela thread "
                    + Thread.currentThread().getName());

            return new ResultadoPedido(pedido, totalReais, totalDolar);
        };
    }

    private ResultadoPedido obterResultado(Future<ResultadoPedido> futuro) {
        try {
            return futuro.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processamento interrompido", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Erro ao processar pedido", e.getCause());
        }
    }
}
