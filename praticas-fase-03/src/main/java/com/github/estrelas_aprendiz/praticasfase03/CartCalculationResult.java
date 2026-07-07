package com.github.estrelas_aprendiz.praticasfase03;

import java.math.BigDecimal;

public record CartCalculationResult(
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal total
) {}
