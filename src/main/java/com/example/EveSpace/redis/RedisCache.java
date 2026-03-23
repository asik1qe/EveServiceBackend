package com.example.EveSpace.redis;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCache {
    private final StringRedisTemplate redis;

    public RedisCache(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void putJson(long key, String json) {
        redis.opsForValue().set(key + "", json);
    }

    public String getJson(long key) {
        return redis.opsForValue().get(key + "");
    }
}
