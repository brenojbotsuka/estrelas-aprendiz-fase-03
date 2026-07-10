package com.github.estrelas_aprendiz.praticasfase03.cupom;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartCalculator {

    // CORREÇÃO: Mudado de List<Coupon.CartItem> para List<CartItem> limpo
    public static CartCalculationResult calculate(List<Coupon.CartItem> items, Coupon coupon, LocalDateTime now) {

        // 1. Calcular subtotal
        BigDecimal subtotal = items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (coupon == null) {
            return new CartCalculationResult(subtotal, BigDecimal.ZERO, subtotal);
        }

        // Cenário 1.1: Validação de data de expiração
        if (coupon.expirationDate().isBefore(now)) {
            throw new IllegalArgumentException("Cupom expirado");
        }

        // Cenário 1.4: Validação de valor mínimo
        if (coupon.minValue() != null && subtotal.compareTo(coupon.minValue()) < 0) {
            throw new IllegalArgumentException("Valor mínimo do pedido não atingido");
        }

        // 2. Calcular o desconto por tipo
        BigDecimal discount = BigDecimal.ZERO;

        // CORREÇÃO: Alterado de PERTENTAGE (com R) para PERCENTAGE (com C) para bater com o seu Enum
        if (coupon.type() == CouponType.PERCENTAGE) {
            // Cenário 1.3: Cupom de Porcentagem
            BigDecimal percentage = coupon.value().divide(BigDecimal.valueOf(100));
            discount = subtotal.multiply(percentage);
        } else if (coupon.type() == CouponType.FIXED) {
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