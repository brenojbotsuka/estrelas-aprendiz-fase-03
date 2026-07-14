package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String descricao;
}
