package com.github.estrelas_aprendiz.praticasfase03.autenticacao.user;

public record UserResponseDTO(
        String email,
        Role role,
        Long id
) {
}
