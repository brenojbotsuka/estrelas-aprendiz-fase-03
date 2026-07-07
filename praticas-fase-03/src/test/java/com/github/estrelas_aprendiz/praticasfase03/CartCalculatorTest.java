package com.github.estrelas_aprendiz.praticasfase03;

import com.github.estrelas_aprendiz.praticasfase03.cupom.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {
    public final LocalDateTime validDate = LocalDateTime.of(2026, 6, 12, 0, 0);

    @Test
    @DisplayName("Cenário 1.1: Deve recusar cupom expirado")
    void deRecusarCupomExpirado() {
        List<CartItem> items = List.of(new CartItem(new BigDecimal("100.00"), 1));

        Coupon expiredCoupon = new Coupon(
                "PROMO10",
                CouponType.FIXED,
                new BigDecimal("10.00"),
                validDate.minusDays(1), //expirado
                null
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> CartCalculator.calculate(items, expiredCoupon, validDate));
        assertEquals("Cupom expirado", exception.getMessage());
    }

    @Test
    @DisplayName("Cenário 1.2: Cupom fixo maior que o carrinho não deve gerar total negativo")
    void cupomFixoMaiorQueCarrinhoNaoDeveGerarTotalNegativo(){
        List<CartItem> items = List.of(new CartItem(new BigDecimal("30.00"), 1));

        Coupon bigCoupon = new Coupon(
                "PROMO50",
                CouponType.FIXED,
                new BigDecimal("50.00"),
                validDate.plusDays(5), // válido
                null
        );
        CartCalculationResult result = CartCalculator.calculate(items, bigCoupon, validDate);
        assertEquals(0, new BigDecimal("30.00").compareTo(result.subTotal()));
        assertEquals(0, new BigDecimal("30.00").compareTo(result.discount())); //Disconto limitado a 30
        assertEquals(0, BigDecimal.ZERO.compareTo(result.total()));
    }
    @Test
    @DisplayName("Cenário 1.3: Cupom de porcentagem deve aplicar desconto corretamente")
    void cupomDePorcentagemDeveAplicarDescontoCorretamente() {
        List<CartItem> items = List.of(new CartItem(new BigDecimal("100.00"), 2));

        Coupon percentageCoupon = new Coupon(
                "PROMO15",
                CouponType.PERTENTAGE,
                new BigDecimal("15.00"), // 15% de desconto
                validDate.plusDays(5), // válido
                null
        );
        CartCalculationResult result = CartCalculator.calculate(items, percentageCoupon, validDate);
        assertEquals(0, new BigDecimal("200.00").compareTo(result.subTotal()));
        assertEquals(0, new BigDecimal("30.00").compareTo(result.discount()));
        assertEquals(0, new BigDecimal("170.00").compareTo(result.total()));
    }
    @Test
    @DisplayName("Cenário 1.4: Cupom com valor mínimo não atingido deve ser recusado")
    void cupomComValorMinimoNaoAtingidoDeveSerRecusado(){
        List<CartItem> items = List.of(new CartItem(new BigDecimal("120.00"), 1));

        Coupon coupon = new Coupon(
                "MIN150",
                CouponType.FIXED,
                new BigDecimal("20.00"),
                validDate.plusDays(5),
                new BigDecimal("150.00")
        );
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> CartCalculator.calculate(items, coupon, validDate));
        assertEquals("Valor mínimo do pedido não atingido", exception.getMessage());
    }
}