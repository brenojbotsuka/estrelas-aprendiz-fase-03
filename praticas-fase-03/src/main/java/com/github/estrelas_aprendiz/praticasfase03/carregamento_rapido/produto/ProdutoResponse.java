package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    private Long id;
    private String nome;
    private double preco;
    private String descricao;
}
