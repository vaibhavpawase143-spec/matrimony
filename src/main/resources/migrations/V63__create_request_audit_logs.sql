-- =====================================================
-- V63__create_request_audit_logs.sql
-- Request Audit Logs Table
-- =====================================================

CREATE TABLE request_audit_logs (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- ACTOR INFORMATION
    -- =====================================================
    actor_id BIGINT,

    actor_name VARCHAR(255),

    actor_type VARCHAR(32) NOT NULL,

    -- =====================================================
    -- REQUEST DETAILS
    -- =====================================================
    http_method VARCHAR(10) NOT NULL,

    request_path VARCHAR(1000) NOT NULL,

    query_string VARCHAR(2000),

    status_code INTEGER NOT NULL,

    outcome VARCHAR(16) NOT NULL,

    failure_type VARCHAR(255),

    ip_address VARCHAR(45),

    user_agent VARCHAR(1000),

    duration_ms BIGINT NOT NULL,

    occurred_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_request_audit_actor_type
        CHECK (
            actor_type IN (
                'ADMIN',
                'USER',
                'SYSTEM',
                'ANONYMOUS'
            )
        ),

    CONSTRAINT chk_request_audit_http_method
        CHECK (
            http_method IN (
                'GET',
                'POST',
                'PUT',
                'PATCH',
                'DELETE',
                'OPTIONS',
                'HEAD'
            )
        ),

    CONSTRAINT chk_request_audit_outcome
        CHECK (
            outcome IN (
                'SUCCESS',
                'FAILURE'
            )
        ),

    CONSTRAINT chk_request_audit_status
        CHECK (
            status_code >= 100
            AND status_code <= 599
        ),

    CONSTRAINT chk_request_audit_duration
        CHECK (
            duration_ms >= 0
        )
);

-- =====================================================
-- ENTITY INDEXES
-- (Matches @Table(indexes = ...))
-- =====================================================

CREATE INDEX idx_request_audit_occurred_at
    ON request_audit_logs(occurred_at);

CREATE INDEX idx_request_audit_actor
    ON request_audit_logs(actor_type, actor_id);

CREATE INDEX idx_request_audit_path
    ON request_audit_logs(request_path);

CREATE INDEX idx_request_audit_status
    ON request_audit_logs(status_code);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_request_audit_method
    ON request_audit_logs(http_method);

CREATE INDEX idx_request_audit_outcome
    ON request_audit_logs(outcome);

CREATE INDEX idx_request_audit_ip
    ON request_audit_logs(ip_address);

CREATE INDEX idx_request_audit_failure
    ON request_audit_logs(failure_type);

CREATE INDEX idx_request_audit_method_status
    ON request_audit_logs(http_method, status_code);

CREATE INDEX idx_request_audit_status_outcome
    ON request_audit_logs(status_code, outcome);

CREATE INDEX idx_request_audit_actor_occurred
    ON request_audit_logs(actor_type, actor_id, occurred_at);

CREATE INDEX idx_request_audit_path_occurred
    ON request_audit_logs(request_path, occurred_at);

CREATE INDEX idx_request_audit_method_occurred
    ON request_audit_logs(http_method, occurred_at);