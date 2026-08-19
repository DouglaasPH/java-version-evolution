# Processador de Pedidos — Java 25

Quinta e última parada do estudo comparativo: o mesmo "mini sistema de
pedidos" das quatro versões anteriores, agora de volta à sintaxe que
você já conhece — só que agora sabendo exatamente qual peça é exclusiva
de cada LTS.

## O que muda em relação ao Java 21

| Arquivo | Mudança | Por quê |
|---|---|---|
| `Main.java` | `public class Main { public static void main(String[] args) }` → `void main()` | Compact Source Files & Instance Main Methods (JEP 512) |

`Produto`, `ItemPedido`, `Pedido`, `ResultadoPedido` continuam records
exatamente como no Java 17. `TipoCliente` continua enum.
`CalculadoraDesconto` não muda — o `switch` expression sobre o enum já
estava com a sintaxe certa desde o 17; pattern matching de `switch`
(JEP 441) só traz ganho real quando o `case` testa *tipo*, não
constante de enum.

## Features de Java 21 usadas de propósito

- **Compact Source Files & Instance Main Methods** (`Main.java`) —
  elimina a obrigatoriedade de `public class` e `static` no `main`.
  Só compila em Java 25; em qualquer versão anterior é erro de
  compilação.
- **Module Import Declarations** (opcional, `Main.java`) — um único
  `import module java.base;` no lugar de vários imports individuais
  (`BigDecimal`, `List`, etc.).

## Estrutura

```
java-25/
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
└── Main.java                   (void main(), sem classe pública)
```

### Pré-requisito: JDK 25 instalado

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
