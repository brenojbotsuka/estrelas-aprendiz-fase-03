package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequest {
    private Long id;
    private String nome;
    private double preco;
    private String descricao;
}
