package com.example.security.ratelimit;

import com.example.config.RateLimitProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RateLimitService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RateLimitProperties properties;

    // Resilient local bounded fallback when Redis is unreachable
    private final Map<String, LocalRateLimitWindow> localFallbackCache = new ConcurrentHashMap<>();

    // Atomic Sliding Window Lua Script
    private static final String SLIDING_WINDOW_LUA = """
            local key = KEYS[1]
            local now = tonumber(ARGV[1])
            local window = tonumber(ARGV[2])
            local limit = tonumber(ARGV[3])
            local clearBefore = now - window
            
            redis.call('ZREMRANGEBYSCORE', key, 0, clearBefore)
            local currentRequests = redis.call('ZCARD', key)
            
            if currentRequests < limit then
                redis.call('ZADD', key, now, now .. '-' .. math.random(1000, 9999))
                redis.call('PEXPIRE', key, window)
                return {1, limit - currentRequests - 1, 0}
            else
                local oldest = redis.call('ZRANGE', key, 0, 0, 'WITHSCORES')
                local retryAfter = 1
                if #oldest > 1 then
                    retryAfter = math.ceil((tonumber(oldest[2]) + window - now) / 1000)
                    if retryAfter < 1 then retryAfter = 1 end
                end
                return {0, 0, retryAfter}
            end
            """;

    private final DefaultRedisScript<List> slidingWindowScript;

    public RateLimitService(StringRedisTemplate stringRedisTemplate, RateLimitProperties properties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.properties = properties;
        this.slidingWindowScript = new DefaultRedisScript<>(SLIDING_WINDOW_LUA, List.class);
    }

    /**
     * Checks if a request is allowed under sliding window limit.
     * Consumes 1 request token if allowed.
     */
    public RateLimitResult tryAcquire(String rawKey, int maxAttempts, int windowSeconds) {
        if (!properties.isEnabled() || maxAttempts <= 0 || windowSeconds <= 0) {
            return RateLimitResult.allow(maxAttempts, maxAttempts);
        }

        String key = "rl:" + rawKey;
        long nowMillis = System.currentTimeMillis();
        long windowMillis = (long) windowSeconds * 1000L;

        try {
            List result = stringRedisTemplate.execute(
                    slidingWindowScript,
                    Collections.singletonList(key),
                    String.valueOf(nowMillis),
                    String.valueOf(windowMillis),
                    String.valueOf(maxAttempts)
            );

            if (result != null && result.size() >= 3) {
                long allowed = ((Number) result.get(0)).longValue();
                long remaining = ((Number) result.get(1)).longValue();
                long retryAfter = ((Number) result.get(2)).longValue();

                if (allowed == 1) {
                    return RateLimitResult.allow(remaining, maxAttempts);
                } else {
                    return RateLimitResult.deny(retryAfter, maxAttempts, maxAttempts);
                }
            }
        } catch (Exception e) {
            log.warn("Redis rate-limiting error for key {}: {}. Activating bounded fallback.", key, e.getMessage());
        }

        // Resilient bounded fallback
        return localSlidingWindow(key, maxAttempts, windowSeconds);
    }

    /**
     * Atomically records a failed attempt with a window TTL.
     */
    public long recordFailedAttempt(String rawKey, int windowSeconds) {
        if (!properties.isEnabled() || windowSeconds <= 0) {
            return 0;
        }

        String key = "rl:fail:" + rawKey;
        try {
            Long count = stringRedisTemplate.opsForValue().increment(key);
            if (count != null && count == 1) {
                stringRedisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
            }
            return count != null ? count : 1;
        } catch (Exception e) {
            log.warn("Redis failed attempt record error for {}: {}", key, e.getMessage());
            return recordLocalFailedAttempt(key, windowSeconds);
        }
    }

    /**
     * Checks if the failed attempt limit has been exceeded.
     */
    public RateLimitResult checkFailedAttempts(String rawKey, int maxAttempts, int windowSeconds) {
        if (!properties.isEnabled() || maxAttempts <= 0) {
            return RateLimitResult.allow(maxAttempts, maxAttempts);
        }

        String key = "rl:fail:" + rawKey;
        try {
            String val = stringRedisTemplate.opsForValue().get(key);
            long attempts = val != null ? Long.parseLong(val) : 0;
            if (attempts >= maxAttempts) {
                Long ttl = stringRedisTemplate.getExpire(key);
                long retryAfter = (ttl != null && ttl > 0) ? ttl : windowSeconds;
                return RateLimitResult.deny(retryAfter, attempts, maxAttempts);
            }
            return RateLimitResult.allow(maxAttempts - attempts, maxAttempts);
        } catch (Exception e) {
            log.warn("Redis failed attempts check error for {}: {}", key, e.getMessage());
            return checkLocalFailedAttempts(key, maxAttempts, windowSeconds);
        }
    }

    /**
     * Resets rate limit / failed attempt counters (e.g. after successful login).
     */
    public void reset(String rawKey) {
        try {
            stringRedisTemplate.delete("rl:" + rawKey);
            stringRedisTemplate.delete("rl:fail:" + rawKey);
        } catch (Exception e) {
            log.warn("Redis reset failed for {}: {}", rawKey, e.getMessage());
        }
        localFallbackCache.remove("rl:" + rawKey);
        localFallbackCache.remove("rl:fail:" + rawKey);
    }

    /**
     * Clears all rate limiting keys and fallback state (useful for test isolation).
     */
    public void clearAll() {
        try {
            java.util.Set<String> keys = stringRedisTemplate.keys("rl:*");
            if (keys != null && !keys.isEmpty()) {
                stringRedisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.warn("Redis clearAll skipped: {}", e.getMessage());
        }
        localFallbackCache.clear();
    }

    // =========================================================================
    // LOCAL RESILIENT FALLBACK (THREAD-SAFE & BOUNDED)
    // =========================================================================

    private synchronized RateLimitResult localSlidingWindow(String key, int maxAttempts, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMillis = (long) windowSeconds * 1000L;

        LocalRateLimitWindow window = localFallbackCache.computeIfAbsent(key, k -> new LocalRateLimitWindow());
        window.cleanup(now - windowMillis);

        if (window.timestamps.size() < maxAttempts) {
            window.timestamps.add(now);
            return RateLimitResult.allow(maxAttempts - window.timestamps.size(), maxAttempts);
        } else {
            long oldest = window.timestamps.get(0);
            long retryAfter = Math.max(1, (oldest + windowMillis - now) / 1000);
            return RateLimitResult.deny(retryAfter, window.timestamps.size(), maxAttempts);
        }
    }

    private synchronized long recordLocalFailedAttempt(String key, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMillis = (long) windowSeconds * 1000L;

        LocalRateLimitWindow window = localFallbackCache.computeIfAbsent(key, k -> new LocalRateLimitWindow());
        window.cleanup(now - windowMillis);
        window.timestamps.add(now);
        return window.timestamps.size();
    }

    private synchronized RateLimitResult checkLocalFailedAttempts(String key, int maxAttempts, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMillis = (long) windowSeconds * 1000L;

        LocalRateLimitWindow window = localFallbackCache.get(key);
        if (window == null) {
            return RateLimitResult.allow(maxAttempts, maxAttempts);
        }
        window.cleanup(now - windowMillis);
        int attempts = window.timestamps.size();
        if (attempts >= maxAttempts) {
            long oldest = window.timestamps.isEmpty() ? now : window.timestamps.get(0);
            long retryAfter = Math.max(1, (oldest + windowMillis - now) / 1000);
            return RateLimitResult.deny(retryAfter, attempts, maxAttempts);
        }
        return RateLimitResult.allow(maxAttempts - attempts, maxAttempts);
    }

    private static class LocalRateLimitWindow {
        final List<Long> timestamps = new java.util.concurrent.CopyOnWriteArrayList<>();

        void cleanup(long expireBefore) {
            timestamps.removeIf(ts -> ts < expireBefore);
        }
    }
}
