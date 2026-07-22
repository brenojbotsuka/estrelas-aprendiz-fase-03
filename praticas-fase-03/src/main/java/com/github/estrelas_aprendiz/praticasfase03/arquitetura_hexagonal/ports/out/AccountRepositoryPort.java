package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.out;

import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.model.Account;

public interface AccountRepositoryPort {
    Account save(Account account);

    boolean existsByCpf(String cpf);
}
