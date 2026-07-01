package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarProdutos () {
    return ResponseEntity.ok(produtoService.listarProdutos());
    }
}
