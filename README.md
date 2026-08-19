# Processador de Pedidos — Java 8 → 11 → 17 → 21 → 25

Um mesmo projeto pequeno, reescrito cinco vezes (uma para cada versão
LTS do Java) para estudar como a linguagem evoluiu na prática, e não
só na teoria. A ideia central: **a lógica de negócio nunca muda**; só
a sintaxe e as ferramentas disponíveis ao redor dela mudam.

## O projeto

Um "mini sistema de pedidos": cadastra produtos, monta pedidos com
desconto por tipo de cliente (Normal, VIP, Atacado), busca a cotação do
dólar numa API pública, processa vários pedidos concorrentemente e
gera um relatório final. Pequeno o bastante para não virar bagunça,
grande o bastante para tocar em modelagem de dados, coleções,
concorrência, I/O e formatação de texto — as áreas onde o Java mais
mudou entre 2014 e 2025.

## Estrutura do repositório

```
.
├── README.md              (este arquivo)
├── java-8/                (base: lambdas, streams, HttpURLConnection, pool de threads)
├── java-11/                (HttpClient novo, var, List.of(), módulos)
├── java-17/                (records, switch expression, text blocks)
├── java-21/                (virtual threads, record patterns, Sequenced Collections)
└── java-25/                (void main() sem classe, import module)
```

Cada pasta é um **projeto Maven independente e completo** — dá para
abrir qualquer uma isoladamente, sem depender das outras. O código de
`java-8` é o ponto de partida; as pastas seguintes são a evolução dele,
uma versão de cada vez.

## Por onde começar

Para a evolução do Java ficar aparente, siga a ordem das pastas: `java-8` →
`java-11` → `java-17` → `java-21` → `java-25`.

Se você já sabe Java 25 e quer ir na direção contrária (o motivo
original deste repositório), a leitura funciona igual, só de trás para
frente: comece por `java-25/README.md`, que aponta o que é exclusivo
dessa versão e não vai existir nas pastas anteriores.

## Pré-requisitos para rodar qualquer uma das versões

- **Maven** instalado (`mvn -version` para conferir).
- **JDK das versões 8, 11, 17, 21 e 25 instaladas.**

## Depois deste repositório

A ideia é repetir o mesmo exercício em **Spring Boot**: pegar esse
domínio de pedidos e transformar numa API REST simples
(`POST /pedidos`, `GET /pedidos/{id}`, `GET /relatorio`), comparando
como a versão do Java escolhida limita ou libera a versão do Spring
Boot disponível (Boot 3.x, por exemplo, exige Java 17+) e como records
e virtual threads aparecem naturalmente em DTOs e configuração de
threads de um projeto web real.