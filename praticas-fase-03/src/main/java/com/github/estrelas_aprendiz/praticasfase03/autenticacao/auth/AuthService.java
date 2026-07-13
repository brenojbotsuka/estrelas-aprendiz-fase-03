package com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth;

import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.debug("Iniciando processo de autenticação no AuthService para o email: {}", request.email());

        try {
            // Tenta autenticar o usuário
            var loginData = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authManager.authenticate(loginData);

            // Extrai o usuário autenticado
            User user = (User) authentication.getPrincipal();

            // Gera o token
            String token = tokenService.generateToken(user);

            return new LoginResponseDTO(token);

        } catch (BadCredentialsException e) {
            log.warn("Falha na autenticação: Credenciais inválidas para o email: {}", request.email());
            // Caso o email ou a senha estejam incorretos
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");

        } catch (Exception e) {
            log.error("Erro inesperado durante o processo de login para o email: {}. Erro: {}", request.email(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar o login: " + e.getMessage());
        }
    }
}
