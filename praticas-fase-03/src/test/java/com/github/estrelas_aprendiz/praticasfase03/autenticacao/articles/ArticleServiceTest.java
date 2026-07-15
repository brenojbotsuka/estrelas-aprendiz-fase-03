package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.Role;
import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private User mockUser;
    @BeforeEach
    public void setUp() {

        mockUser = new User();
        mockUser.setId(99L);
        mockUser.setEmail("autor@email.com");
        mockUser.setPassword("senha_criptografada");
        mockUser.setRole(Role.ROLE_AUTHOR);

        SecurityContext securityContext = mock(SecurityContext.class);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void deveCriarArtigoPreenchendoOAutorProgramaticamente() {
        ArticleRequestDTO dto = new ArticleRequestDTO("Título de Teste", "Conteúdo do artigo...");
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> {
            Article arg = invocation.getArgument(0);
            arg.setId(1L);
            return arg;
        });

        ArticleResponseDTO savedArticleDto = articleService.createArticle(dto);

        // Asserções no DTO de resposta
        assertNotNull(savedArticleDto);
        assertEquals(1L, savedArticleDto.getId());
        assertEquals("Título de Teste", savedArticleDto.getTitle());
        assertEquals("Conteúdo do artigo...", savedArticleDto.getContent());
        assertEquals("autor@email.com", savedArticleDto.getAuthor()); // Valida o e-mail que o DTO carrega

        // Captura o Article salvo para garantir que a entidade foi montada corretamente nos bastidores
        ArgumentCaptor<Article> articleCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository, times(1)).save(articleCaptor.capture());

        Article capturedArticle = articleCaptor.getValue();
        assertEquals(99L, capturedArticle.getAuthor().getId());
        assertEquals("autor@email.com", capturedArticle.getAuthor().getEmail());
    }
}
