package org.example;

import org.example.entities.Pedido;
import org.example.entities.ResultadoPedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProcessadorPedidos {
    private static final long LATENCIA_SIMULADA_MS = 500;

    private final CotacaoService cotacaoService;

    public ProcessadorPedidos(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    public List<ResultadoPedido> processar(List<Pedido> pedidos) throws InterruptedException {
        BigDecimal cotacaoDolar = cotacaoService.buscarCotacaoDolar();
        System.out.println("Cotação do dólar obtida: R$ " + cotacaoDolar);

        List<Future<ResultadoPedido>> futuros = new ArrayList<>();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Pedido pedido : pedidos) {
                futuros.add(executor.submit(criarTarefa(pedido, cotacaoDolar)));
            }
        }

        List<ResultadoPedido> resultados = new ArrayList<>();
        for (Future<ResultadoPedido> futuro : futuros) {
            resultados.add(obterResultado(futuro));
        }

        return resultados;
    }

    private Callable<ResultadoPedido> criarTarefa(Pedido pedido, BigDecimal cotacaoDolar) {
        return () -> {
            Thread.sleep(LATENCIA_SIMULADA_MS);

            BigDecimal totalReais = CalculadoraDesconto.calcularTotalComDesconto(pedido);
            BigDecimal totalDolar = totalReais.divide(cotacaoDolar, 2, RoundingMode.HALF_UP);

            System.out.println("Pedido #" + pedido.id() + " processado por " + Thread.currentThread());

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
