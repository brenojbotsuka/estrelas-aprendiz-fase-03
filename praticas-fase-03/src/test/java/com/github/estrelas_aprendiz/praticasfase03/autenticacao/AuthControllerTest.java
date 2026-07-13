package com.github.estrelas_aprendiz.praticasfase03.autenticacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Desativa filtros do Spring Security para testar a lógica pura da controller isolada
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthService authService;


    @Test
    void login_ShouldReturn200AndToken_WhenCredentialsAreValid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user@email.com", "password123");

        // Cria o objeto que o Controller espera receber do Service
        var responseDTO = new LoginResponseDTO("mocked-jwt-token", "Bearer");

        // Ensina o authService a retornar esse DTO quando for chamado
        Mockito.when(authService.login(Mockito.any(LoginRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsAreInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("wrong@email.com", "wrongpass");

        // Força o authService a lançar o erro de credenciais inválidas
        Mockito.when(authService.login(Mockito.any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}