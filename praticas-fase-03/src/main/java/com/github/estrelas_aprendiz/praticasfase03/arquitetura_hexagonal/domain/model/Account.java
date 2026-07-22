package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.model;

import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private static final BigDecimal MINIMUM_DEPOSIT = new BigDecimal("50.00");

    private final UUID id;
    private final String holderName;
    private final String cpf;
    private BigDecimal balance;

    private Account(UUID id, String holderName, String cpf, BigDecimal initialDeposit) {
        this.id = UUID.randomUUID();
        this.holderName = holderName;
        this.cpf = cpf;
        this.balance = initialDeposit;
    }

    public static Account create(String holderName, String cpf, BigDecimal initialDeposit) {
        if (initialDeposit == null || initialDeposit.compareTo(MINIMUM_DEPOSIT) < 0) {
            throw new DomainException("O depósito inicial deve ser de no mínimo R$ 50,00.");
        }
        if (holderName == null || holderName.isBlank()) {
            throw new DomainException("O nome do titular é obrigatório.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new DomainException("O CPF é obrigatório.");
        }

        return new Account(UUID.randomUUID(), holderName, cpf, initialDeposit);
    }

    // Getters para expor o estado do objeto
    public UUID getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
