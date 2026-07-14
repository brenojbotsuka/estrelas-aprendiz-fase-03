package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    public List<ProdutoResponse> listarProdutos() {
        String cacheKey = "products:all";
        String cachedJson = null;

        // Tenta buscar no Redis
        try {
            cachedJson = cacheService.get(cacheKey);
        } catch (Exception e) {
            log.warn("Redis indisponível ao tentar buscar cache. Redirecionando requisição para o banco de dados. Erro: {}", e.getMessage());
        }

        // CACHE HIT: O Redis tinha os dados
        if (cachedJson != null) {
            try {
                log.info("Cache HIT para a chave: {}", cacheKey);
                return objectMapper.readValue(
                        cachedJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ProdutoResponse.class)
                );
            } catch (Exception e) {
                log.error("Erro ao desserializar JSON do cache. Buscando do banco de dados.", e);
            }
        }

        // CACHE MISS: Busca as entidades do banco de dados
        log.info("Cache MISS para a chave: {}", cacheKey);
        List<Produto> produtos = produtoRepository.findAll();

        List<ProdutoResponse> produtoResponse = produtos.stream()
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getPreco(), p.getDescricao()))
                .toList();

        // Tenta salvar a lista de response no Redis
        try {
            String jsonToCache = objectMapper.writeValueAsString(produtoResponse);
            cacheService.set(cacheKey, jsonToCache);
        } catch (Exception e) {
            log.warn("Redis indisponível ao tentar salvar cache. O fluxo continuará normalmente. Erro: {}", e.getMessage());
        }

        return produtoResponse;
    }
}

