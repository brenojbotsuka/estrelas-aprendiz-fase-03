package com.github.estrelas_aprendiz.praticasfase03.autenticacao.user;

public record UserResponseDTO(
        String email,
        String password,
        Role role,
        Long id
) {
}
