package com.github.estrelas_aprendiz.praticasfase03.frete;

import org.springframework.beans.factory.annotation.Value; // IMPORTANTE: Adicionado este import
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class LogistiticsClient {
    private final RestTemplate restTemplate;
    private final String logisticsUrl;

    // CORREÇÃO AQUI: Adicionada a anotação @Value para o Spring saber de onde tirar a URL
    public LogistiticsClient(RestTemplate restTemplate, @Value("${logistics.api.url}") String logisticsUrl) {
        this.restTemplate = restTemplate;
        this.logisticsUrl = logisticsUrl;
    }

    public BigDecimal fetchShippingCost(String cep) {
        try {
            // Garante que a URL monte o caminho completo esperado pelos seus testes do WireMock
            String url = logisticsUrl + "/api/v1/shipping/" + cep;

            // Cenário 2.2: Consome a Api externa esperando um JSON
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("shippingCost")) {
                return new BigDecimal(response.get("shippingCost").toString());
            }
            throw new RuntimeException("Erro da api externa do frete");
        } catch (Exception e) {
            System.err.println("Falha ao cotar frete para o cep " + cep + ". Aplicando fallback de R$ 30.00. Motivo: " + e.getMessage());
            return new BigDecimal("30.00"); // Cenário 2.3: Tarifa padrão (Fallback)
        }
    }
}