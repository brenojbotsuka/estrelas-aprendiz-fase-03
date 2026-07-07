package com.github.estrelas_aprendiz.praticasfase03.carregamento_rapido.produto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    private final StringRedisTemplate redisTemplate;
    private final long ttlSeconds;

    public CacheService(StringRedisTemplate redisTemplate, @Value("${redis.ttl.seconds}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.ttlSeconds = ttlSeconds;
    }

    // Guarda dados temporários e evita cálculos repetidos ou consultas demoradas
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
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
