package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.service;

import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.exception.DomainException;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.model.Account;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.in.CreateAccountCommand;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.in.CreateAccountUseCase;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.out.AccountRepositoryPort;

public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepositoryPort accountRepositoryPort;

    public CreateAccountService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account createAccount(CreateAccountCommand command) {
        if (accountRepositoryPort.existsByCpf(command.cpf())) {
            throw new DomainException("CPF já cadastrado");
        }

        Account account = Account.create(
                command.holderName(),
                command.cpf(),
                command.initialDeposit()
        );

        return accountRepositoryPort.save(account);
    }
}
