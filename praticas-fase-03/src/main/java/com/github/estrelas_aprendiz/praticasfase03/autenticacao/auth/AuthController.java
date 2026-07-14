package com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Tentativa de login iniciada para o usuário: {}", request.email());

        try {
            LoginResponseDTO response = authService.login(request);

            log.info("Login realizado com sucesso para o usuário: {}", request.email());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            log.error("Erro ao tentar realizar login para o usuário: {}. Erro: {}", request.email(), e.getMessage());
            throw e;
        }
    }
}