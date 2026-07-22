package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.in;

import java.math.BigDecimal;

public record CreateAccountCommand(
        String holderName,
        String cpf,
        BigDecimal initialDeposit
) {
}
