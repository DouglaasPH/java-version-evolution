package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CotacaoService {
    private static final String URL_API = "https://economia.awesomeapi.com.br/last/USD-BRL";
    private static final Pattern PADRAO_BID = Pattern.compile("\"bid\"\\s*:\\s*\"([0-9.]+)\"");

    private final HttpClient client = HttpClient.newHttpClient();

    public BigDecimal buscarCotacaoDolar() {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API respondeu com status " + response.statusCode());
            }

            return extrairCotacao(response.body());
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível buscar a cotação (" + e.getMessage() + "). Usando valor de fallback.");
            return new BigDecimal("5.00");
        }
    }

    private BigDecimal extrairCotacao(String jsonBruto) {
        Matcher matcher = PADRAO_BID.matcher(jsonBruto);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        throw new IllegalStateException("Não foi possível extrair a cotação da resposta da API.");
    }
}
