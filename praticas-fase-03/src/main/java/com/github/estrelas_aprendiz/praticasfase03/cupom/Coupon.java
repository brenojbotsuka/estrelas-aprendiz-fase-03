package com.github.estrelas_aprendiz.praticasfase03.cupom;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Coupon(
        String code,
        CouponType type,
        BigDecimal value,
        LocalDateTime expirationDate, // Data de expiração do cupom
        BigDecimal minValue // Valor mínimo do pedido para aplicar o cupom
) {}
