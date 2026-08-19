# Processador de Pedidos — Java 11

Segunda parada do estudo comparativo: o mesmo "mini sistema de pedidos"
do projeto Java 8, agora adaptado para usar o que ficou disponível no Java 11.


## O que muda em relação ao Java 8

| Arquivo | Mudança | Por quê |
|---|---|---|
| `CotacaoService.java` | `HttpURLConnection` → `java.net.http.HttpClient` | cliente HTTP oficial, chegou no Java 11 (JEP 321) |
| `ProcessadorPedidos.java` | tipos explícitos → `var` | inferência de variável local (JEP 286, do Java 10) |
| `Main.java` | `Arrays.asList()` → `List.of()` | métodos de fábrica de coleções imutáveis (Java 9) |

O resto do projeto (`Produto`, `ItemPedido`, `Pedido`, `TipoCliente`,
`CalculadoraDesconto`, `ResultadoPedido`, `GeradorRelatorio`) continua
**idêntico** ao Java 8.

## Features de Java 9/10/11 usadas de propósito

- **`HttpClient` novo** (`CotacaoService`) — suporta HTTP/1.1 e HTTP/2,
  síncrono e assíncrono. Muito mais enxuto que `HttpURLConnection`: não
  precisa mais gerenciar `InputStreamReader` linha por linha nem chamar
  `disconnect()` manualmente num `finally`.
- **`var`** (`ProcessadorPedidos`) — inferência de tipo para variáveis
  locais. O compilador continua checando tipos normalmente; só a
  declaração fica mais curta.
- **`List.of()`** (`Main`) — cria listas **verdadeiramente imutáveis**
  (lançam exceção em qualquer tentativa de alteração), diferente de
  `Arrays.asList()`, que é só de tamanho fixo.

## O que foi removido e pode dar problema numa migração real

Isso não afeta este projeto (que não usa nada disso), mas é o principal
motivo de dor de cabeça ao migrar projetos **reais** de Java 8 para 11:

- **Java EE embutido removido** (JEP 320): `javax.xml.bind` (JAXB),
  `javax.xml.ws` (JAX-WS), `javax.activation`, CORBA e
  `javax.transaction` saem do JDK. Se um projeto usava essas APIs sem
  saber que vinham "de graça" no classpath, ele quebra com
  `ClassNotFoundException` ao migrar — e precisa adicionar as
  dependências manualmente via Maven/Gradle.
- **Nashorn** (motor JavaScript embutido) fica deprecado (removido de
  vez só no Java 15).

## Estrutura

```
pedidos-java8/
├── pom.xml
└── src/main/java/org/example/
    ├── entities/
    │   ├── enums/
    │   │    └── TipoCliente.java        (sem mudanças)
    │   │
    │   ├── Produto.java            (sem mudanças)
    │   ├── ItemPedido.java         (sem mudanças)
    │   ├── Pedido.java             (sem mudanças)
    │   └── ResultadoPedido.java    (sem mudanças)
    │
    ├── CotacaoService.java         (reescrito com HttpClient)
    ├── ProcessadorPedidos.java     (agora com var)
    ├── GeradorRelatorio.java       (sem mudanças)
    ├── CalculadoraDesconto.java    (sem mudanças)
    └── Main.java                   (agora com List.of())
```

### Pré-requisito: JDK 11 instalado


## Saída esperada (resumida)

Igual ao projeto Java 8 — a lógica de negócio não mudou, só a sintaxe
por baixo:

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

## Checklist rápido para revisar seu código

- [ ] `CotacaoService` não importa mais `java.net.HttpURLConnection`
- [ ] `CotacaoService` importa `java.net.http.HttpClient`,
  `HttpRequest`, `HttpResponse`
- [ ] `ProcessadorPedidos` usa `var pool = ...` e
  `var futuros = new ArrayList<Future<ResultadoPedido>>()`
  (repare que o tipo genérico precisa ficar explícito do lado
  direito quando se usa `var`)
- [ ] `Main` usa `List.of(...)` em vez de `Arrays.asList(...)` para
  montar os itens de cada pedido
