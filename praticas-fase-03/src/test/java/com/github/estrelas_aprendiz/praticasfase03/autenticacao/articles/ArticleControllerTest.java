package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.auth.TokenService;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.config.SecurityConfig;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.config.SecurityFilter;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
@Import({SecurityConfig.class, SecurityFilter.class, ArticleController.class})
class ArticleControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /articles: Usuário READER deve receber 403 Forbidden")
    public void criarArtigo_ComPerfilReader_DeveRetornarForbidden() throws Exception {
        ArticleRequestDTO dto = new ArticleRequestDTO("Título Proibido", "Conteúdo qualquer");

        mockMvc.perform(post("/articles")
                        .with(user("usuarioReader").roles("READER")) // Simula ROLE_READER
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /articles: Usuário AUTHOR deve passar pela segurança e retornar 201 Created")
    public void criarArtigo_ComPerfilAuthor_DeveRetornarCreated() throws Exception {
        ArticleRequestDTO dto = new ArticleRequestDTO("Título Permitido", "Conteúdo qualquer");

        ArticleResponseDTO mockResponse = ArticleResponseDTO.builder()
                .id(1L)
                .title("Título Permitido")
                .content("Conteúdo qualquer")
                .author("autor@email.com")
                .build();

        Mockito.when(articleService.createArticle(Mockito.any(ArticleRequestDTO.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/articles")
                        .with(user("usuarioAuthor").roles("AUTHOR"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /articles: Usuário READER deve conseguir listar artigos (200 OK)")
    public void listarArtigos_ComPerfilReader_DeveRetornarOk() throws Exception {
        Mockito.when(articleService.getAllArticles()).thenReturn(List.of());

        mockMvc.perform(get("/articles")
                        .with(user("usuarioReader").roles("READER"))) // Simula ROLE_READER
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /articles: Usuário AUTHOR também deve conseguir listar artigos (200 OK)")
    public void listarArtigos_ComPerfilAuthor_DeveRetornarOk() throws Exception {
        Mockito.when(articleService.getAllArticles()).thenReturn(List.of());

        mockMvc.perform(get("/articles")
                        .with(user("usuarioAuthor").roles("AUTHOR"))) // Simula ROLE_AUTHOR
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /articles/{id}: Usuário READER deve receber 403 Forbidden")
    public void deletarArtigo_ComPerfilReader_DeveRetornarForbidden() throws Exception {
        mockMvc.perform(delete("/articles/1")
                        .with(user("usuarioReader").roles("READER"))
                        .with(csrf())) // Simula ROLE_READER
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /articles/{id}: Usuário AUTHOR deve passar e retornar 204 No Content")
    public void deletarArtigo_ComPerfilAuthor_DeveRetornarNoContent() throws Exception {
        Mockito.doNothing().when(articleService).deleteArticle(1L);

        mockMvc.perform(delete("/articles/1")
                        .with(user("usuarioAuthor").roles("AUTHOR"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}