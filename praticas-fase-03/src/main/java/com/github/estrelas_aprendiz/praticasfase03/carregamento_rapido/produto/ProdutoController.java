package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;


    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() throws Exception {
        List<ProdutoResponse> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }
}


