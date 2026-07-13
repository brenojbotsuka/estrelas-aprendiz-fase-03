package com.github.estrelas_aprendiz.praticasfase03;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void permitirAcessoPublicoAoLogin() throws Exception{
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"teste@email.com\",\"password\":\"123456\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void negarAcessoSemAutenticacao() throws Exception{
        mockMvc.perform(post("/api/protected"))
                .andExpect(status().isUnauthorized());
    }

}

