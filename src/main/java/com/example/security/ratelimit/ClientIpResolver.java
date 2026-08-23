package com.example.security.ratelimit;

import com.example.config.RateLimitProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientIpResolver {

    private final RateLimitProperties properties;

    /**
     * Resolves the real client IP address safely.
     * If trusted proxies are configured and the remote address is a trusted proxy,
     * inspects X-Forwarded-For (taking the leftmost valid client IP) or X-Real-IP.
     * Otherwise, uses request.getRemoteAddr() to prevent IP spoofing attacks.
     */
    public String resolveClientIp(HttpServletRequest request) {
        if (request == null) {
            return "127.0.0.1";
        }

        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr == null || remoteAddr.isBlank()) {
            remoteAddr = "127.0.0.1";
        }

        List<String> trustedProxies = properties.getTrustedProxies();
        boolean isTrustedProxy = trustedProxies != null && !trustedProxies.isEmpty() &&
                (trustedProxies.contains("*") || trustedProxies.contains(remoteAddr));

        if (isTrustedProxy) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                String[] parts = xForwardedFor.split(",");
                if (parts.length > 0) {
                    String clientIp = parts[0].trim();
                    if (isValidIp(clientIp)) {
                        return clientIp;
                    }
                }
            }

            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isBlank() && isValidIp(xRealIp.trim())) {
                return xRealIp.trim();
            }
        }

        return remoteAddr;
    }

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isBlank() || ip.length() > 45) {
            return false;
        }
        // Basic sanity check to prevent injection into Redis keys
        return ip.matches("^[a-zA-Z0-9.:%_-]+$");
    }
}
