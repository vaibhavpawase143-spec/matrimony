-- =====================================================
-- V64__create_audit_logs.sql
-- Audit Logs Table
-- =====================================================

CREATE TABLE audit_logs (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- ADMIN INFORMATION
    -- =====================================================
    admin_id BIGINT NOT NULL,

    admin_email VARCHAR(255),

    -- =====================================================
    -- AUDIT DETAILS
    -- =====================================================
    action VARCHAR(100) NOT NULL,

    entity_type VARCHAR(100) NOT NULL,

    entity_id BIGINT NOT NULL,

    old_values TEXT,

    new_values TEXT,

    description TEXT,

    ip_address VARCHAR(45),

    user_agent TEXT,

    status VARCHAR(50),

    error_message TEXT,

    suspicious_activity BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_audit_log_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_audit_log_status
        CHECK (
            status IS NULL OR
            status IN (
                'SUCCESS',
                'FAILURE'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- (Matches @Table(indexes = ...))
-- =====================================================

CREATE INDEX idx_admin_id
    ON audit_logs(admin_id);

CREATE INDEX idx_entity_type
    ON audit_logs(entity_type);

CREATE INDEX idx_entity_id
    ON audit_logs(entity_id);

CREATE INDEX idx_action
    ON audit_logs(action);

CREATE INDEX idx_created_at
    ON audit_logs(created_at);

CREATE INDEX idx_ip_address
    ON audit_logs(ip_address);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_audit_status
    ON audit_logs(status);

CREATE INDEX idx_audit_suspicious
    ON audit_logs(suspicious_activity);

CREATE INDEX idx_audit_admin_created
    ON audit_logs(admin_id, created_at);

CREATE INDEX idx_audit_entity
    ON audit_logs(entity_type, entity_id);

CREATE INDEX idx_audit_action_created
    ON audit_logs(action, created_at);

CREATE INDEX idx_audit_status_created
    ON audit_logs(status, created_at);

CREATE INDEX idx_audit_suspicious_created
    ON audit_logs(suspicious_activity, created_at);

CREATE INDEX idx_audit_admin_action
    ON audit_logs(admin_id, action);

CREATE INDEX idx_audit_entity_action
    ON audit_logs(entity_type, entity_id, action);