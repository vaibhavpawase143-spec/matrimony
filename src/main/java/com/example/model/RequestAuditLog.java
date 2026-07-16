package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Immutable audit record for a state-changing HTTP request. Request bodies are
 * deliberately not stored so passwords, tokens, and personal data cannot leak
 * into the audit trail. The route is retained, but not its query parameters.
 */
@Getter
@Setter
@Entity
@Table(name = "request_audit_logs", indexes = {
        @Index(name = "idx_request_audit_occurred_at", columnList = "occurred_at"),
        @Index(name = "idx_request_audit_actor", columnList = "actor_type,actor_id"),
        @Index(name = "idx_request_audit_path", columnList = "request_path"),
        @Index(name = "idx_request_audit_status", columnList = "status_code")
})
public class RequestAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_name", length = 255)
    private String actorName;

    @Column(name = "actor_type", nullable = false, length = 32)
    private String actorType;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "request_path", nullable = false, length = 1000)
    private String requestPath;

    @Column(name = "query_string", length = 2000)
    private String queryString;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "outcome", nullable = false, length = 16)
    private String outcome;

    @Column(name = "failure_type", length = 255)
    private String failureType;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "duration_ms", nullable = false)
    private long durationMs;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt;
}
