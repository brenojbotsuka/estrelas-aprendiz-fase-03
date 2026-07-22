package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.in;

import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.model.Account;

public interface CreateAccountUseCase {
    Account createAccount(CreateAccountCommand command);
}
