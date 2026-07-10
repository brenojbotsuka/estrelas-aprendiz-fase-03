package com.github.estrelas_aprendiz.praticasfase03.frete;
import com.github.estrelas_aprendiz.praticasfase03.cupom.CartCalculator;
import com.github.estrelas_aprendiz.praticasfase03.cupom.Coupon;
import com.github.estrelas_aprendiz.praticasfase03.cupom.CouponEntity;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "logistics.api.url=http://localhost:8089")
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8089)
class OrderServiceTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private com.github.estrelas_aprendiz.praticasfase03.frete.OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("2.1 - deve validar cupom existente e ativo no banco")
    void validarCupomExistenteEAtivo() {
        // GARANTIA: Salva o cupom fisicamente no banco H2 antes de rodar a consulta do serviço
      CouponEntity cupomValido = new CouponEntity();
        cupomValido.setCode("PROMO2026");
        cupomValido.setType("PERCENTAGE");
        cupomValido.setValue(new BigDecimal("20.00"));
        cupomValido.setExpirationDate(LocalDateTime.now().plusDays(10));
        cupomValido.setMinValue(BigDecimal.ZERO);
        cupomValido.setActive(true);

        couponRepository.save(cupomValido); // Insere direto na memória do H2

        List<Coupon.CartItem> items = List.of(new Coupon.CartItem(new BigDecimal("100.00"), 1));
        CartFinalResponse result = orderService.calculateCart(items, "PROMO2026", "01001-000");

        assertNotNull(result);
        assertEquals(0, new BigDecimal("20.00").compareTo(result.discount()));
    }

    @Test
    @DisplayName("2.2 - deve mapear o JSON de frete retornando com sucesso")
    void deveMapearJsonFreteComSucesso() {
        // CORREÇÃO: Alinhada a URL para o padrão das outras rotas executadas pelo LogisticsClient
        stubFor(get(urlEqualTo("/api/v1/shipping/01001-000"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"shippingCost\": 15.00}")));

        List<Coupon.CartItem> items = List.of(new Coupon.CartItem(new BigDecimal("100.00"), 1));

        com.github.estrelas_aprendiz.praticasfase03.frete.CartFinalResponse result = orderService.calculateCart(items, null, "01001-000");

        assertEquals(0, new BigDecimal("15.00").compareTo(result.shippingCost()));
        assertEquals(0, new BigDecimal("115.00").compareTo(result.totalFinal()));
    }

    @Test
    @DisplayName("Cenário 2.3: Deve acionar fallback de R$ 30,00 se a API externa der erro 500")
    void deveAplicarFallbackSeAPIFalhar() {
        stubFor(get(urlEqualTo("/api/v1/shipping/99999-999"))
                .willReturn(aResponse().withStatus(500)));

        List<Coupon.CartItem> items = List.of(new Coupon.CartItem(new BigDecimal("100.00"), 1));

        com.github.estrelas_aprendiz.praticasfase03.frete.CartFinalResponse result = orderService.calculateCart(items, null, "99999-999");

        // O fallback de tratamento de erro do seu LogisticsClient deve capturar o 500 e fixar em 30.00
        assertEquals(0, new BigDecimal("30.00").compareTo(result.shippingCost()));
        assertEquals(0, new BigDecimal("130.00").compareTo(result.totalFinal()));
    }

    @Test
    @DisplayName("Cenário 2.4: Execução fim-a-fim de endpoint POST local")
    void deveExecutarEndpointComSucessoEEstruturaMatematicaExata() throws Exception {
        stubFor(get(urlEqualTo("/api/v1/shipping/01001-000"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"shippingCost\": 15.00}")));

        String requestJson = """
                {
                    "items": [{"price": 100.00, "quantity": 2}],
                    "couponCode": "PROMO2026",
                    "cep": "01001-000"
                }
                """;

        mockMvc.perform(post("/api/v1/cart/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtotal").value(200.00))
                .andExpect(jsonPath("$.discount").value(40.00))
                .andExpect(jsonPath("$.shippingCost").value(15.00))
                .andExpect(jsonPath("$.totalFinal").value(175.00));
    }
}