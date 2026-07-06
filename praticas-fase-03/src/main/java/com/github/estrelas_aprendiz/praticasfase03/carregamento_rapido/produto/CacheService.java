package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final StringRedisTemplate redisTemplate;

    // Guarda dados temporários e evita cálculos repetidos ou consultas demoradas
    public void set(String key, String value, long timeoutInMinutes) {
        redisTemplate.opsForValue().set(key, value, timeoutInMinutes, TimeUnit.MINUTES);
    }
    //Recupera rapidamente dados temporários sem consultar o banco ou recalcular
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    // Invalida ou remove um dado do cache
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
