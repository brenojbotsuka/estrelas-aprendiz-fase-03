package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.service;

import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.exception.DomainException;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.model.Account;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.in.CreateAccountCommand;
import com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.ports.out.AccountRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @InjectMocks
    private CreateAccountService createAccountService;

    @Test
    @DisplayName("Deve lançar DomainException quando o depósito inicial for inferior a R$ 50,00")
    void mostreDomainExceptionQuandoDepositoForInferiorAoMinimo() {

        var command = new CreateAccountCommand("João Silva", "12345678900", new BigDecimal("49.99"));
        when(accountRepositoryPort.existsByCpf(command.cpf())).thenReturn(false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            createAccountService.createAccount(command);
        });

        assertEquals("O depósito inicial deve ser de no mínimo R$ 50,00.", exception.getMessage());
        verify(accountRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o CPF já existir no repositório")
    void shouldThrowDomainExceptionWhenCpfAlreadyExists() {
        // Arrange
        var command = new CreateAccountCommand("Maria Souza", "12345678900", new BigDecimal("100.00"));
        when(accountRepositoryPort.existsByCpf(command.cpf())).thenReturn(true);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            createAccountService.createAccount(command);
        });

        assertEquals("CPF já cadastrado", exception.getMessage());
        verify(accountRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar e retornar a conta estruturada com sucesso")
    void shouldCreateAccountSuccessfully() {
        // Arrange
        var command = new CreateAccountCommand("Carlos Oliveira", "98765432100", new BigDecimal("150.00"));
        when(accountRepositoryPort.existsByCpf(command.cpf())).thenReturn(false);
        when(accountRepositoryPort.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account createdAccount = createAccountService.createAccount(command);

        // Assert
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getId());
        assertEquals("Carlos Oliveira", createdAccount.getHolderName());
        assertEquals("98765432100", createdAccount.getCpf());
        assertEquals(new BigDecimal("150.00"), createdAccount.getBalance());

        verify(accountRepositoryPort, times(1)).save(any(Account.class));
    }
}

