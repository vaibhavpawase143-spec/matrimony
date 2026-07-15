package com.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestInfoUtil {

    public String getIpAddress() {

        try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder
                            .currentRequestAttributes())
                            .getRequest();

            String forwarded = request.getHeader("X-Forwarded-For");

            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }

            return request.getRemoteAddr();

        } catch (Exception e) {

            return "UNKNOWN";
        }
    }

    public String getUserAgent() {

        try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder
                            .currentRequestAttributes())
                            .getRequest();

            String userAgent = request.getHeader("User-Agent");

            return userAgent != null ? userAgent : "UNKNOWN";

        } catch (Exception e) {

            return "UNKNOWN";
        }
    }
}