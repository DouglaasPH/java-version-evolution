# Processador de Pedidos — Java 21

Quarta parada do estudo comparativo: o mesmo "mini sistema de pedidos"
dos projetos anteriores, agora usando a LTS considerada a mais
importante desde o Java 8 — principalmente pela chegada das
**virtual threads**.

## O que muda em relação ao Java 17

| Arquivo | Mudança | Por quê |
|---|---|---|
| `ProcessadorPedidos.java` | pool fixo (`newFixedThreadPool`) → `newVirtualThreadPerTaskExecutor()` | Virtual Threads (JEP 444) |
| `ProcessadorPedidos.java` | `shutdown()` + `awaitTermination()` manuais → `try-with-resources` | `ExecutorService` ganhou `close()` automático (Java 19+) |
| `GeradorRelatorio.java` | acesso normal ao record → *record pattern* num `instanceof` | JEP 440, para marcar pedidos de atacado |
| `GeradorRelatorio.java` | `list.get(0)` / `list.get(size()-1)` → `getFirst()` / `getLast()` | Sequenced Collections (JEP 431) |

`Produto`, `ItemPedido`, `Pedido`, `ResultadoPedido` continuam records
exatamente como no Java 17. `TipoCliente` continua enum.
`CalculadoraDesconto` não muda — o `switch` expression sobre o enum já
estava com a sintaxe certa desde o 17; pattern matching de `switch`
(JEP 441) só traz ganho real quando o `case` testa *tipo*, não
constante de enum.

## Features de Java 21 usadas de propósito

- **Virtual Threads** (`ProcessadorPedidos`) — a mudança mais visível
  do projeto. Em vez de um pool fixo de 4 threads reais, cada pedido
  ganha sua própria thread virtual, criada e descartada sob demanda.
  Com só 4 pedidos você não sente diferença de desempenho — o ganho
  aparece de verdade se você simular centenas ou milhares de pedidos
  simultâneos.
- **`ExecutorService` com `close()` automático** — desde o Java 19,
  `ExecutorService` implementa `AutoCloseable`. O `try-with-resources`
  já espera todas as tarefas enviadas terminarem antes de continuar,
  então o `finally` manual do projeto anterior some.
- **Record Patterns** (`GeradorRelatorio`) — desestruturar um `Pedido`
  direto na condição do `if (pedido instanceof Pedido(...))`, extraindo
  os campos como variáveis locais sem chamar os acessores um por um.
- **Sequenced Collections** (`GeradorRelatorio`) — `getFirst()` e
  `getLast()` em vez de `get(0)` e `get(size() - 1)`, deixando a
  intenção explícita e evitando erro de índice em lista vazia.

## Estrutura

```
java-21/
├── pom.xml
└── src/main/java/org/example/
├── entities/
│   ├── enums/
│   │    └── TipoCliente.java   (sem mudanças)
│   │
│   ├── Produto.java            (sem mudanças)
│   ├── ItemPedido.java         (sem mudanças)
│   ├── Pedido.java             (sem mudanças)
│   └── ResultadoPedido.java    (sem mudanças)
│
├── CotacaoService.java         (sem mudanças)
├── ProcessadorPedidos.java     (virtual threads + try-with-resources)
├── GeradorRelatorio.java       (record pattern + Sequenced Collections)
├── CalculadoraDesconto.java    (sem mudanças)
└── Main.java                   (sem mudanças)
```

### Pré-requisito: JDK 21 instalado

## Saída esperada (resumida)

A lógica de negócio não mudou — só a forma de processar concorrência e
o relatório final ganharam um toque a mais:

```
Processando 4 pedido(s)...
 
Cotação do dólar obtida: R$ 5.43
Pedido #1 processado por VirtualThread[#34]/runnable
Pedido #2 processado por VirtualThread[#37]/runnable
...
 
========================================
 RELATORIO DE PEDIDOS PROCESSADOS
========================================
Pedido #3 ⭐ (atacado, 2 itens)
  Cliente : Distribuidora XYZ (ATACADO)
  ...
----------------------------------------
TOTAL GERAL: R$ ...
Primeiro pedido processado: #1
Último pedido processado:   #4
========================================
 
Tempo total de processamento: ~500ms
```
