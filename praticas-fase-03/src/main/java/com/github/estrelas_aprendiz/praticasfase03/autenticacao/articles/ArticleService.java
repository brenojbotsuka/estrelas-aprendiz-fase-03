package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Artigo não encontrado");
        }
        articleRepository.deleteById(id);
    }
}