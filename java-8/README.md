# Processador de Pedidos — Java 8

Projeto de estudo: um "mini sistema de pedidos" escrito em Java 8 puro
(sem frameworks, sem dependências externas), pensado para depois ser
reescrito nas versões 11, 17, 21 e 25 e comparar como a mesma lógica
muda de sintaxe entre elas.

## O que o programa faz

1. Cadastra alguns produtos e monta 4 pedidos de exemplo, com clientes
   de tipos diferentes (Normal, VIP, Atacado).
2. Busca a cotação atual do dólar numa API pública
   (`economia.awesomeapi.com.br`), usando `HttpURLConnection` — o
   cliente HTTP "manual" que existia antes do `java.net.http` (que só
   chega no Java 11). Se a API não responder (sem internet, indisponível
   etc.), o programa usa um valor de cotação fixo (fallback) e continua
   normalmente.
3. Processa os pedidos **concorrentemente** usando um `ExecutorService`
   com um pool fixo de 4 threads — a ferramenta de concorrência
   "clássica" do Java, disponível desde sempre. Cada pedido simula um
   pequeno atraso de I/O (`Thread.sleep`) para você sentir, no relógio,
   a diferença entre processar em sequência e processar em paralelo.
4. Aplica desconto por tipo de cliente (Normal: 0%, VIP: 10%, Atacado: 20%).
5. Converte o total de cada pedido para dólar usando a cotação buscada.
6. Imprime um relatório final no console.

## Features de Java 8 usadas de propósito

- **Lambdas** (`ProcessadorPedidos.criarTarefa`) — a novidade mais
  importante do Java 8, usada para implementar a interface funcional
  `Callable`.
- **`ExecutorService` / pool de threads tradicional** — antes de Virtual
  Threads (Java 21), é assim que se faz concorrência em Java.
- Nada de `var`, `record`, `sealed`, pattern matching ou text blocks —
  todos esses recursos ainda não existiam. As classes de dados
  (`Produto`, `Pedido`, etc.) são escritas "na mão", com construtor e
  getters explícitos.

## Estrutura

```
pedidos-java8/
├── pom.xml
└── src/main/java/org/example/
    ├── entities/
    │   ├── enums/
    │   │    └── TipoCliente.java        (enum com desconto)
    │   │
    │   ├── Produto.java            (classe de dados)
    │   ├── ItemPedido.java         (classe de dados)
    │   ├── Pedido.java             (classe de dados)
    │   └── ResultadoPedido.java    (classe de dados)
    │
    ├── CotacaoService.java     (chamada HTTP)
    ├── ProcessadorPedidos.java (concorrência com ExecutorService)
    ├── GeradorRelatorio.java
    ├── CalculadoraDesconto.java
    └── Main.java               (ponto de entrada)
```

### Pré-requisito: JDK 8 instalado

## Saída esperada (resumida)

```
Processando 4 pedido(s)...

Cotação do dólar obtida: R$ 5.43
Pedido #1 processado pela thread pool-1-thread-1
Pedido #2 processado pela thread pool-1-thread-2
Pedido #3 processado pela thread pool-1-thread-3
Pedido #4 processado pela thread pool-1-thread-4

========================================
 RELATORIO DE PEDIDOS PROCESSADOS
========================================

Pedido #1
  Cliente : Ana Silva (NORMAL)
  - 1x Notebook (subtotal: R$ 3500.00)
  - 1x Mouse sem fio (subtotal: R$ 80.00)
  Total   : R$ 3580.00  (US$ 659.30)
...
----------------------------------------
TOTAL GERAL: R$ ...
========================================

Tempo total de processamento: ~500ms
```

Repare que o tempo total fica próximo de **500ms**, e não `4 x 500ms =
2000ms` — isso é a concorrência em ação: os 4 pedidos são processados
ao mesmo tempo, cada um numa thread do pool, em vez de um esperar o
outro terminar.
