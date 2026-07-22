package com.github.estrelas_aprendiz.praticasfase03.arquitetura_hexagonal.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
