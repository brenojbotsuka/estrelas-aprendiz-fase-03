package com.github.estrelas_aprendiz.praticasfase03.autenticacao.Articles;


import com.github.estrelas_aprendiz.praticasfase03.autenticacao.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Article createArticle(ArticleRequestDTO dto){
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
        return articleRepository.save(article);
    }

}
