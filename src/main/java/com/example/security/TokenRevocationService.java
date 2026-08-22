package com.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenRevocationService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtUtil jwtUtil;

    // Bounded in-memory fallback cache in case Redis is temporarily unreachable
    private final ConcurrentHashMap<String, Long> localRevocationFallback = new ConcurrentHashMap<>();

    private static final String REVOKED_KEY_PREFIX = "rl:jwt:revoked:";

    /**
     * Revokes an access token by storing its JTI or secure fingerprint in Redis
     * with a TTL matching its exact remaining lifespan.
     */
    public void revokeToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }

        try {
            String identifier = getRevocationIdentifier(token);
            if (identifier == null) {
                return;
            }

            Date expiration = jwtUtil.extractExpiration(token);
            long remainingMillis = expiration != null ? (expiration.getTime() - System.currentTimeMillis()) : 0;
            if (remainingMillis <= 0) {
                return; // Already expired naturally
            }

            long ttlSeconds = Math.max(1, remainingMillis / 1000);
            String key = REVOKED_KEY_PREFIX + identifier;

            try {
                stringRedisTemplate.opsForValue().set(key, "revoked", Duration.ofSeconds(ttlSeconds));
            } catch (Exception e) {
                log.warn("Redis unavailable during token revocation. Storing in local fallback cache: {}", e.getMessage());
                localRevocationFallback.put(identifier, System.currentTimeMillis() + remainingMillis);
            }
        } catch (Exception e) {
            log.error("Failed to revoke token: {}", e.getMessage());
        }
    }

    /**
     * Checks whether an access token has been revoked prior to expiration.
     */
    public boolean isRevoked(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        try {
            String identifier = getRevocationIdentifier(token);
            if (identifier == null) {
                return false;
            }

            String key = REVOKED_KEY_PREFIX + identifier;

            try {
                Boolean hasKey = stringRedisTemplate.hasKey(key);
                if (Boolean.TRUE.equals(hasKey)) {
                    return true;
                }
            } catch (Exception e) {
                log.warn("Redis unavailable during token revocation check. Checking local fallback: {}", e.getMessage());
            }

            Long localExpiry = localRevocationFallback.get(identifier);
            if (localExpiry != null) {
                if (localExpiry > System.currentTimeMillis()) {
                    return true;
                } else {
                    localRevocationFallback.remove(identifier);
                }
            }

            return false;
        } catch (Exception e) {
            log.error("Failed to check token revocation: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Purges all revocation cache entries (used during test teardown/isolation).
     */
    public void clearAll() {
        try {
            Set<String> keys = stringRedisTemplate.keys(REVOKED_KEY_PREFIX + "*");
            if (keys != null && !keys.isEmpty()) {
                stringRedisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.warn("Redis clearAll revocation keys skipped: {}", e.getMessage());
        }
        localRevocationFallback.clear();
    }

    private String getRevocationIdentifier(String token) {
        String jti = jwtUtil.extractJti(token);
        if (jti != null && !jti.trim().isEmpty()) {
            return jti.trim();
        }
        // Fallback to MD5 hex fingerprint of token string if JTI is absent
        return DigestUtils.md5DigestAsHex(token.getBytes(StandardCharsets.UTF_8));
    }
}
