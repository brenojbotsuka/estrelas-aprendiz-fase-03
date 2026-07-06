


package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    public List<ProdutoResponse> listarProdutos() {
        // Chave usada para buscar ou trocar o cache dessa lista
        String cacheKey = "products:all";

        try {
            // Tenta ecuperar os produtos em formato JSON armazenados no Redis
            String cachedJson = cacheService.get(cacheKey);

            if (cachedJson != null) {
                // Desserializa e retorna caso haja cache
                System.out.println("CACHE HIT");
                return objectMapper.readValue(
                        cachedJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ProdutoResponse.class)
                );
            }

            System.out.println("CACHE MISS");

            // Caso não houver cache, busca do banco e mapeia para DTO
            List<ProdutoResponse> produtosDto = produtoRepository.findAll().stream()
                    .map(p -> new ProdutoResponse(
                            p.getId(),
                            p.getNome(),
                            p.getPreco(),
                            p.getDescricao()
                    ))
                    .toList();

            // Salva a lista de DTOs serializada no cache (expira em 10 minutos)
            String jsonToCache = objectMapper.writeValueAsString(produtosDto);
            cacheService.set(cacheKey, jsonToCache, 10);

            return produtosDto;

        } catch (Exception e) {
            // Tratamento de erro caso a serialização ou o Redis falhem.
            System.err.println("Erro ao manipular o cache: " + e.getMessage());

            return produtoRepository.findAll().stream()
                    .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getPreco(), p.getDescricao()))
                    .toList();
        }
    }
}

