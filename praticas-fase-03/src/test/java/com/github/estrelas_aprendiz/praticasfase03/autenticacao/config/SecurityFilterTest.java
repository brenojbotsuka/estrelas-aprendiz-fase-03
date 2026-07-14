package com.github.estrelas_aprendiz.praticasfase03.autenticacao.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "api.security.token.secret=segredo_de_teste_super_secreto_e_longo_12345",
        "api.security.token.expiration=3600000"})
@AutoConfigureMockMvc
class SecurityFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 401 Unauthorized ao tentar acessar rota protegida sem o cabeçalho Authorization")
    void deveRetornar401SemHeader() throws Exception {
        mockMvc.perform(get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized ao tentar acessar rota protegida com token inválido")
    void deveRetornar401ComTokenInvalido() throws Exception {
        mockMvc.perform(get("/articles")
                        .header("Authorization", "Bearer token_completamente_invalido_12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized ao passar formato de cabeçalho incorreto (sem prefixo Bearer)")
    void deveRetornar401SemPrefixoBearer() throws Exception {
        mockMvc.perform(get("/articles")
                        .header("Authorization", "12345InvalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}