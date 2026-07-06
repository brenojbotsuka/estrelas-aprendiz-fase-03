package com.github.estrelas_aprendiz.praticasfase03;

import java.math.BigDecimal;

public record CartItem(
    BigDecimal price,
    int quantity
)  {

}
