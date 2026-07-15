package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestDTO {
    private String title;
    private String content;
}
