package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.AssertionFailure;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponseDTO createArticle(ArticleRequestDTO dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!(principal instanceof User)){
            throw new IllegalStateException("Usuário não está autenticado");
        }
        User currentUser = (User) principal;

        Article article = Article.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(currentUser)
                .build();

        return ArticleResponseDTO.builder()
                .id(articleRepository.save(article).getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(currentUser.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> ArticleResponseDTO.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .author(article.getAuthor().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteArticle(Long id) throws AccessDeniedException {
      Article article = articleRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Artigo com id "+ id + "não encontrado."));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!(principal instanceof User)){
            throw new AccessDeniedException("Usuário não autenticado.");
        }
        User correntUser = (User) principal;

        if (!(correntUser.getId().equals(article.getAuthor().getId()))){
            throw new AccessDeniedException("Você não tem permição para deletar o artigo de outro autor.");
        }
        articleRepository.delete(article);

    }
}