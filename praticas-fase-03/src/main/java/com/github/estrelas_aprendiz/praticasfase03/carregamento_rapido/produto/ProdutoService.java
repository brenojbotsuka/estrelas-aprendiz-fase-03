package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public List<ProdutoResponse> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(p -> new ProdutoResponse(
                        p.getId(),
                        p.getNome(),
                        p.getPreco(),
                        p.getDescricao()
                ))
                .toList();
    }
}