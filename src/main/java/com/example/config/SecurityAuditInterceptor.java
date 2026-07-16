package com.example.config;

import com.example.util.AuditContext;
import com.example.security.CustomUserDetails;
import com.example.model.RequestAuditLog;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import com.example.service.RequestAuditLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.time.Instant;

/**
 * HTTP Interceptor for automatic audit context population.
 * Extracts the current user from Spring Security and sets it in AuditContext.
 * 
 * This ensures that all entity modifications are automatically tracked with the user who made them.
 * The interceptor:
 * 1. Sets the audit user at the beginning of each request
 * 2. Clears it at the end to prevent ThreadLocal memory leaks
 */
@Component
@RequiredArgsConstructor
public class SecurityAuditInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SecurityAuditInterceptor.class);
    private static final String STARTED_AT = SecurityAuditInterceptor.class.getName() + ".startedAt";
    private static final String ACTOR_ID = SecurityAuditInterceptor.class.getName() + ".actorId";
    private static final String ACTOR_NAME = SecurityAuditInterceptor.class.getName() + ".actorName";
    private static final String ACTOR_TYPE = SecurityAuditInterceptor.class.getName() + ".actorType";

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RequestAuditLogService requestAuditLogService;

    /**
     * Called BEFORE the request is handled.
     * Sets the current user ID from Spring Security context into AuditContext.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(STARTED_AT, Instant.now());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                // Try to extract user ID from principal
                Object principal = authentication.getPrincipal();
                
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                    // For standard UserDetails, extract username
                    String username = userDetails.getUsername();
                    // Note: You may need to adjust this to extract actual user ID from your custom UserDetails
                    // For now, using username as identifier
                    AuditContext.setCurrentUserName(username);
                    if (principal instanceof CustomUserDetails customUserDetails) {
                        AuditContext.setCurrentUserId(customUserDetails.getId());
                        setActor(request, customUserDetails.getId(), username, "USER");
                    } else {
                        resolveActor(request, username, authentication);
                    }
                } else if (principal instanceof String) {
                    // Fallback to string principal
                    String username = (String) principal;
                    AuditContext.setCurrentUserName(username);
                    resolveActor(request, username, authentication);
                }
            }
        } catch (Exception e) {
            log.warn("Could not establish audit actor for request {}", request.getRequestURI(), e);
        }
        
        return true;
    }

    /**
     * Called AFTER the view has been rendered.
     * Clears the AuditContext to prevent memory leaks in ThreadPool environments.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            recordRequest(request, response, ex);
            AuditContext.clear();
        } catch (Exception e) {
            log.error("Could not persist audit record for request {}", request.getRequestURI(), e);
            AuditContext.clear();
        }
    }

    private void resolveActor(HttpServletRequest request, String username, Authentication authentication) {
        boolean administrator = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()) || "ROLE_SUPER_ADMIN".equals(authority.getAuthority()));

        if (administrator) {
            adminRepository.findByEmailIgnoreCase(username).ifPresent(admin ->
                    setActor(request, admin.getId(), admin.getEmail(), "ADMIN"));
            return;
        }

        userRepository.findByEmailIgnoreCase(username).ifPresent(user -> {
            AuditContext.setCurrentUserId(user.getId());
            setActor(request, user.getId(), user.getEmail(), "USER");
        });
    }

    private void setActor(HttpServletRequest request, Long actorId, String actorName, String actorType) {
        request.setAttribute(ACTOR_ID, actorId);
        request.setAttribute(ACTOR_NAME, actorName);
        request.setAttribute(ACTOR_TYPE, actorType);
    }

    private void recordRequest(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if (!isAuditableRequest(request)) {
            return;
        }

        RequestAuditLog auditLog = new RequestAuditLog();
        auditLog.setActorId((Long) request.getAttribute(ACTOR_ID));
        auditLog.setActorName((String) request.getAttribute(ACTOR_NAME));
        auditLog.setActorType((String) request.getAttribute(ACTOR_TYPE) == null ? "ANONYMOUS" : (String) request.getAttribute(ACTOR_TYPE));
        auditLog.setHttpMethod(request.getMethod());
        auditLog.setRequestPath(request.getRequestURI());
        // Query parameters can contain credentials or reset/verification tokens.
        // Store the route only; request payloads and parameters are never audited.
        auditLog.setStatusCode(response.getStatus());
        auditLog.setOutcome(exception == null && response.getStatus() < 400 ? "SUCCESS" : "FAILURE");
        auditLog.setFailureType(exception == null ? null : exception.getClass().getSimpleName());
        auditLog.setIpAddress(clientIp(request));
        auditLog.setUserAgent(truncate(request.getHeader("User-Agent"), 1000));
        auditLog.setDurationMs(duration(request));
        auditLog.setOccurredAt(java.time.LocalDateTime.now());
        requestAuditLogService.record(auditLog);
    }

    private boolean isAuditableRequest(HttpServletRequest request) {
        String method = request.getMethod();
        boolean stateChanging = "POST".equals(method) || "PUT".equals(method)
                || "PATCH".equals(method) || "DELETE".equals(method);
        return stateChanging && !"/api/chat/ping".equals(request.getRequestURI());
    }

    private long duration(HttpServletRequest request) {
        Object startedAt = request.getAttribute(STARTED_AT);
        return startedAt instanceof Instant instant ? Duration.between(instant, Instant.now()).toMillis() : 0L;
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        return forwardedFor == null || forwardedFor.isBlank() ? request.getRemoteAddr() : forwardedFor.split(",")[0].trim();
    }

    private String truncate(String value, int maximumLength) {
        return value == null ? null : value.substring(0, Math.min(value.length(), maximumLength));
    }
}

