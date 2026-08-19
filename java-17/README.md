# Processador de Pedidos — Java 17

Terceira parada do estudo comparativo: o mesmo "mini sistema de
pedidos" dos projetos Java 8 e 11, agora reescrito com a sintaxe
moderna consolidada no Java 17.

## O que muda em relação ao Java 11

| Arquivo | Mudança | Por quê |
|---|---|---|
| `Produto.java` | classe → `record` | dados imutáveis, gerados automaticamente (JEP 395) |
| `ItemPedido.java` | classe → `record` (com método extra) | idem, mas mostrando que records podem ter métodos próprios |
| `Pedido.java` | classe → `record` com *compact constructor* | idem, com validação/normalização no construtor |
| `ResultadoPedido.java` | classe → `record` | idem, record "puro" |
| `CalculadoraDesconto.java` | `switch` statement → `switch` expression | sintaxe consolidada (chegou no 14, mas é hora de usar) |
| `GeradorRelatorio.java` | concatenação de `\n` → *text block* no cabeçalho fixo | JEP 378 |
| `ProcessadorPedidos.java`, `CotacaoService.java` | `.getId()`, `.getNome()` etc. → `.id()`, `.nome()` etc. | records não usam prefixo `get` nos acessores |

## Features de Java 17 usadas de propósito

- **Records** (`Produto`, `ItemPedido`, `Pedido`, `ResultadoPedido`) —
  substituem ~40 linhas de construtor/getters/`equals`/`hashCode`/
  `toString` escritos à mão por uma única linha de declaração. O
  compilador gera tudo isso automaticamente.
- **Compact constructor** (`Pedido`) — permite validar ou normalizar um
  valor recebido (aqui, tornar a lista de itens imutável) sem precisar
  reescrever o construtor inteiro.
- **Switch expression** (`CalculadoraDesconto`) — sem `break`, sem
  `default` obrigatório quando todos os valores do enum são cobertos, e
  o compilador avisa se você esquecer algum.
- **Text blocks** (`GeradorRelatorio`) — string de várias linhas sem
  precisar escapar `\n` ou concatenar com `+`.

## Estrutura

```
java-17/
├── pom.xml
└── src/main/java/org/example/
├── entities/
│   ├── enums/
│   │    └── TipoCliente.java   (sem mudanças)
│   │
│   ├── Produto.java            (record)
│   ├── ItemPedido.java         (record)
│   ├── Pedido.java             (record com compact constructor)
│   └── ResultadoPedido.java    (record)
│
├── CotacaoService.java         (sem mudanças)
├── ProcessadorPedidos.java     (só ajusta os acessores)
├── GeradorRelatorio.java       (text block no cabeçalho)
├── CalculadoraDesconto.java    (switch expression)
└── Main.java                   (sem mudanças)
```

### Pré-requisito: JDK 17 instalado

## Saída esperada (resumida)

A lógica de negócio não mudou — só a sintaxe por baixo — então a saída
é igual à dos projetos anteriores:

```
Processando 4 pedido(s)...

Cotação do dólar obtida: R$ 5.43
Pedido #1 processado pela thread pool-1-thread-1
Pedido #2 processado pela thread pool-1-thread-2
...

========================================
 RELATORIO DE PEDIDOS PROCESSADOS
========================================
...
Tempo total de processamento: ~500ms
```
