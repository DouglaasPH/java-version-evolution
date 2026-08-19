package org.example;

import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProcessadorPedidos {
    private static final int TAMANHO_POOL = 4;
    private static final long LATENCIA_SIMULADA_MS = 500;

    private final CotacaoService cotacaoService;

    public ProcessadorPedidos(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    public List<ResultadoPedido> processar(List<Pedido> pedidos) throws InterruptedException {
        BigDecimal cotacaoDolar = cotacaoService.buscarCotacaoDolar();
        System.out.println("Cotação do dólar obtida: R$ " + cotacaoDolar);

        var pool = Executors.newFixedThreadPool(TAMANHO_POOL);
        var futuros = new ArrayList<Future<ResultadoPedido>>();

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
