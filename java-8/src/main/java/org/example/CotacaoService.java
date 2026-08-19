package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Busca a cotação atual do dólar numa API pública.
 *
 * Em Java 8 o cliente HTTP "oficial" ainda não existe (java.net.http só
 * chega no Java 11) — então o jeito padrão da época é usar
 * HttpURLConnection, que é mais verboso e exige gerenciar manualmente
 * o InputStream de resposta.
 *
 * Como este é um projeto "Java puro" (sem dependências externas), o JSON
 * de resposta é lido com uma regex simples em vez de uma biblioteca de
 * parsing — suficiente para extrair um único campo numérico.
 */
public class CotacaoService {
    private static final String URL_API = "https://economia.awesomeapi.com.br/last/USD-BRL";
    private static final Pattern PADRAO_BID = Pattern.compile("\"bid\"\\s*:\\s*\"([0-9.]+)\"");

    /**
     * Retorna a cotação do dólar em reais. Se a API falhar por qualquer
     * motivo (sem internet, fora do ar, etc.), devolve um valor fixo de
     * fallback para o programa continuar funcionando.
     */
    public BigDecimal buscarCotacaoDolar() {
        HttpURLConnection conexao = null;

        try {
            URL url = new  URL(URL_API);
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(5000);
            conexao.setReadTimeout(5000);

            int status = conexao.getResponseCode();

            if (status != 200) {
                throw new IOException("API respondeu com status " + status);
            }

            String corpo = lerCorpoResposta(conexao);
            return extrairCotacao(corpo);

        } catch (Exception e) {
            System.out.println("Aviso: não foi possível buscar a cotação (" + e.getMessage() + "). Usando valor de fallback.");
            return new BigDecimal("5.00");
        } finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }

    private String lerCorpoResposta(HttpURLConnection conexao) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                builder.append(linha);
            }
        }
        return builder.toString();
    }

    private BigDecimal extrairCotacao(String jsonBruto) {
        Matcher matcher = PADRAO_BID.matcher(jsonBruto);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        throw new IllegalStateException("Não foi possível extrair a cotação da resposta da API.");
    }
}
