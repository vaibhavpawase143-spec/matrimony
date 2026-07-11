package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void evictUserMatches(Long userId) {

        // matches cache
        String matchPattern = "matches::" + userId + "_*";

        Set<String> matchKeys = redisTemplate.keys(matchPattern);
        if (matchKeys != null && !matchKeys.isEmpty()) {
            redisTemplate.delete(matchKeys);
        }

        // top matches cache
        String topMatchPattern = "top_matches::" + userId + "_*";

        Set<String> topKeys = redisTemplate.keys(topMatchPattern);
        if (topKeys != null && !topKeys.isEmpty()) {
            redisTemplate.delete(topKeys);
        }
    }
}