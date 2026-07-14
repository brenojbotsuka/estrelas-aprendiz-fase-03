package com.github.estrelas_aprendiz.praticasfase03.autenticacao.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth.TokenService;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.Role; // ajuste para o pacote correto do seu enum Role
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("Deve gerar um token JWT válido com claims e tempo de expiração corretos")
    void shouldGenerateValidToken() {

        User user = new User();
        user.setId(123L);
        user.setEmail("usuario@teste.com");
        user.setRole(Role.ROLE_AUTHOR);

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length, "O token deve ter 3 partes separadas por pontos (Header, Payload, Signature)");
        DecodedJWT decodedJWT = JWT.decode(token);


        assertEquals("usuario@teste.com", decodedJWT.getSubject());
        assertEquals(123L, decodedJWT.getClaim("id").asLong());
        assertEquals("ROLE_AUTHOR", decodedJWT.getClaim("role").asString());
        assertEquals("auth-api", decodedJWT.getIssuer());


        Date expiration = decodedJWT.getExpiresAt();
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()), "O token deve expirar no futuro");

        long diferencaSegundos = (expiration.getTime() - System.currentTimeMillis()) / 1000;
        assertTrue(diferencaSegundos > 3595 && diferencaSegundos <= 3600,
                "A expiração deve ser de aproximadamente 3600 segundos (1 hora)");
    }

    @Test
    @DisplayName("Deve extrair com sucesso o e-mail (subject) de um token válido")
    void shouldExtractSubjectSuccessfully() {
        User user = new User();
        user.setId(789L);
        user.setEmail("teste@sucesso.com");
        user.setRole(Role.ROLE_AUTHOR);

        String token = tokenService.generateToken(user);
        String subject = tokenService.validateTokenAndGetSubject(token);

        assertEquals("teste@sucesso.com", subject);
    }

    @Test
    @DisplayName("Deve retornar nulo ao tentar extrair dados de um token inválido ou corrompido")
    void shouldReturnNullWhenTokenIsInvalid() {
        String subject = tokenService.validateTokenAndGetSubject("token.totalmente.invalido");

        assertNull(subject);
    }
}