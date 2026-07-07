package com.github.estrelas_aprendiz.praticasfase03;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartCalculator {


    public static CartCalculationResult calculate(List<CartItem> items, Coupon coupon, LocalDateTime now) {

        // 1. Calcular subtotal (Ajustado 'subTotal' para 'subtotal' minúsculo para bater com o resto do código)
        BigDecimal subtotal = items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (coupon == null) {
            return new CartCalculationResult(subtotal, BigDecimal.ZERO, subtotal);
        }

        // Cenário 1.1
        if (coupon.expirationDate().isBefore(now)) {
            throw new IllegalArgumentException("Cupom expirado");
        }

        // Cenário 1.4 (Garantido que usa cupon.minValue() e compara com subtotal)
        if (coupon.minValue() != null && subtotal.compareTo(coupon.minValue()) < 0) {
            throw new IllegalArgumentException("Valor mínimo do pedido não atingido");
        }

        // 2. Calcular o desconto por tipo (Substituído 'coupon' por 'cupon' e 'subtotal' corrigido)
        BigDecimal discount = BigDecimal.ZERO;
        if (coupon.type() == CouponType.Percentage) {
            // Cenário 1.3: Cupom de Porcentagem
            BigDecimal percentage = coupon.value().divide(BigDecimal.valueOf(100));
            discount = subtotal.multiply(percentage);
        } else if (coupon.type() == CouponType.Fixed) {
            // Cupom de Valor Fixo
            discount = coupon.value();
        }

        // Cenário 1.2: Evitar que o total fique negativo
        BigDecimal totalProducts = subtotal.subtract(discount);
        if (totalProducts.compareTo(BigDecimal.ZERO) < 0) {
            totalProducts = BigDecimal.ZERO;
        }

        // Ajusta o valor visual do desconto
        BigDecimal finalDiscount = subtotal.subtract(totalProducts);

        return new CartCalculationResult(subtotal, finalDiscount, totalProducts);
    }
}