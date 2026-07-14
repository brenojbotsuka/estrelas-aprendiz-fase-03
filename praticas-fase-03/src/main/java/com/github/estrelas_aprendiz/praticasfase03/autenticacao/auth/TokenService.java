package com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${JWT_SECRET}")
    private String secret;

    // GERAÇÃO DO TOKEN
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-api")               // Identifica quem gerou o token
                    .withSubject(user.getEmail())         // Guarda o identificador do usuário (geralmente o email/username)
                    .withExpiresAt(genExpirationDate())    // Define o tempo de expiração
                    .sign(algorithm);                     // Assina com o algoritmo secreto

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    // VALIDAÇÃO DO TOKEN
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)    // Valida a assinatura e a expiração do token de forma automática
                    .getSubject();    // Retorna o email do usuário caso esteja tudo correto

        } catch (JWTVerificationException exception) {
            // Retorna vazio se o token for inválido, expirado ou adulterado
            return "";
        }
    }

    // Define o tempo de expiração
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
