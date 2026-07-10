package com.github.estrelas_aprendiz.praticasfase03.frete;

import com.github.estrelas_aprendiz.praticasfase03.cupom.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.github.estrelas_aprendiz.praticasfase03.cupom.CouponType;

@Service
public class OrderService {

    private final CouponRepository couponRepository;
    private final LogistiticsClient logisticsClient;

    public OrderService(CouponRepository couponRepository, LogistiticsClient logisticsClient) {
        this.couponRepository = couponRepository;
        this.logisticsClient = logisticsClient;
    }


    // 1. CORREÇÃO DA ASSINATURA: Mudado de List<Coupon.CartItem> para List<CartItem>
    public CartFinalResponse calculateCart(List<Coupon.CartItem> items, String couponCode, String cep) {
        // Cenário 2.1: Validação e busca no banco de dados
        Coupon domainCoupon = null;

        if (couponCode != null && !couponCode.isBlank()) {
            CouponEntity entity = couponRepository.findByCodeAndActiveTrue(couponCode)
                    .orElseThrow(() -> new IllegalArgumentException("Cupom inválido ou inativo"));

            domainCoupon = new Coupon(
                    entity.getCode(),
                    CouponType.valueOf(entity.getType().toUpperCase().trim()),
                    entity.getValue(), // O valor do desconto do cupom
                    entity.getExpirationDate(),
                    entity.getMinValue()
            );
        }

        // Executa cálculo base da Task 1
        CartCalculationResult calcResult = CartCalculator.calculate(items, domainCoupon, LocalDateTime.now());

        // Busca o frete via API Externa / Fallback
        BigDecimal shippingCost = logisticsClient.fetchShippingCost(cep);

        // Calcula total final: (Produtos - Desconto) + Frete
        BigDecimal totalFinal = calcResult.total().add(shippingCost);

        return new CartFinalResponse(
                calcResult.subTotal(),
                calcResult.discount(),
                shippingCost,
                totalFinal
        );
    }
}

// Cenário 2.4
record CartFinalResponse(BigDecimal subtotal, BigDecimal discount, BigDecimal shippingCost, BigDecimal totalFinal) {}
