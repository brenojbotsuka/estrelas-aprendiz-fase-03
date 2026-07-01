package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido;

import com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
