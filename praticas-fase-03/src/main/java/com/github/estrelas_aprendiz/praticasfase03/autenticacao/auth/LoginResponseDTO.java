package com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth;


public record LoginResponseDTO(String token, String tokenType) {
    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
