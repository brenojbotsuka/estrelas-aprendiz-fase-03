package com.github.estrelas_aprendiz.praticasfase03.autenticacao.articles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
}
