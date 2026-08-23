package com.example.config;

import com.example.util.AuditContext;
import com.example.util.LogSanitizer;
import com.example.security.CustomUserDetails;
import com.example.security.ratelimit.ClientIpResolver;
import com.example.model.RequestAuditLog;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import com.example.service.RequestAuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * HTTP Interceptor for automatic audit context population and request tracking.
 * Extracts the current user from Spring Security and sets it in AuditContext.
 * Also logs state-changing requests safely in RequestAuditLog without secrets.
 */
@Component
public class SecurityAuditInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SecurityAuditInterceptor.class);
    private static final String STARTED_AT = SecurityAuditInterceptor.class.getName() + ".startedAt";
    private static final String ACTOR_ID = SecurityAuditInterceptor.class.getName() + ".actorId";
    private static final String ACTOR_NAME = SecurityAuditInterceptor.class.getName() + ".actorName";
    private static final String ACTOR_TYPE = SecurityAuditInterceptor.class.getName() + ".actorType";
    private static final String REQUEST_ID_ATTR = "requestId";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RequestAuditLogService requestAuditLogService;
    private final ClientIpResolver clientIpResolver;

    public SecurityAuditInterceptor(
            UserRepository userRepository,
            AdminRepository adminRepository,
            @Lazy RequestAuditLogService requestAuditLogService,
            ClientIpResolver clientIpResolver
    ) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.requestAuditLogService = requestAuditLogService;
        this.clientIpResolver = clientIpResolver;
    }

    /**
     * Called BEFORE the request is handled.
     * Sets the current user ID from Spring Security context into AuditContext and establishes Request ID.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(STARTED_AT, Instant.now());

        // Establish traceable Request ID
        String incomingRequestId = request.getHeader(REQUEST_ID_HEADER);
        String requestId = (incomingRequestId != null && !incomingRequestId.isBlank())
                ? LogSanitizer.sanitizeAndTruncate(incomingRequestId, 64)
                : UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID_ATTR, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);
        MDC.put(REQUEST_ID_ATTR, requestId);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                    String username = userDetails.getUsername();
                    AuditContext.setCurrentUserName(username);
                    if (principal instanceof CustomUserDetails customUserDetails) {
                        AuditContext.setCurrentUserId(customUserDetails.getId());
                        setActor(request, customUserDetails.getId(), username, "USER");
                    } else {
                        resolveActor(request, username, authentication);
                    }
                } else if (principal instanceof String) {
                    String username = (String) principal;
                    AuditContext.setCurrentUserName(username);
                    resolveActor(request, username, authentication);
                }
            }
        } catch (Exception e) {
            log.warn("Could not establish audit actor for request {}", LogSanitizer.sanitize(request.getRequestURI()), e);
        }

        return true;
    }

    /**
     * Called AFTER the view has been rendered.
     * Clears the AuditContext and MDC to prevent memory leaks in ThreadPool environments.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            recordRequest(request, response, ex);
        } catch (Exception e) {
            log.error("Could not persist audit record for request {}", LogSanitizer.sanitize(request.getRequestURI()), e);
        } finally {
            AuditContext.clear();
            MDC.remove(REQUEST_ID_ATTR);
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
        auditLog.setActorName(LogSanitizer.sanitizeAndTruncate((String) request.getAttribute(ACTOR_NAME), 255));
        auditLog.setActorType((String) request.getAttribute(ACTOR_TYPE) == null ? "ANONYMOUS" : (String) request.getAttribute(ACTOR_TYPE));
        auditLog.setHttpMethod(request.getMethod());
        auditLog.setRequestPath(LogSanitizer.sanitizeAndTruncate(request.getRequestURI(), 1000));
        // Query parameters can contain credentials or reset/verification tokens.
        // Store the route only; request payloads and parameters are never audited.
        auditLog.setStatusCode(response.getStatus());
        auditLog.setOutcome(exception == null && response.getStatus() < 400 ? "SUCCESS" : "FAILURE");
        auditLog.setFailureType(exception == null ? null : LogSanitizer.sanitizeAndTruncate(exception.getClass().getSimpleName(), 255));
        auditLog.setIpAddress(LogSanitizer.sanitizeAndTruncate(clientIpResolver.resolveClientIp(request), 45));
        auditLog.setUserAgent(LogSanitizer.sanitizeAndTruncate(request.getHeader("User-Agent"), 1000));
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
}

