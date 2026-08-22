package com.example.util;

import com.example.security.ratelimit.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class RequestInfoUtil {

    private final ClientIpResolver clientIpResolver;

    public String getIpAddress() {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder
                            .currentRequestAttributes())
                            .getRequest();

            return LogSanitizer.sanitizeAndTruncate(clientIpResolver.resolveClientIp(request), 45);
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    public String getUserAgent() {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder
                            .currentRequestAttributes())
                            .getRequest();

            String userAgent = request.getHeader("User-Agent");
            return LogSanitizer.sanitizeAndTruncate(userAgent != null ? userAgent : "UNKNOWN", 1000);
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}