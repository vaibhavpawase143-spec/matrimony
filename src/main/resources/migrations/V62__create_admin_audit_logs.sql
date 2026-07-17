-- =====================================================
-- V62__create_admin_audit_logs.sql
-- Admin Audit Logs Table
-- =====================================================

CREATE TABLE admin_audit_logs (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- AUDIT INFORMATION
    -- =====================================================
    module VARCHAR(255) NOT NULL,

    admin_id BIGINT NOT NULL,

    action VARCHAR(255) NOT NULL,

    entity_type VARCHAR(255) NOT NULL,

    entity_id BIGINT,

    description TEXT,

    old_value TEXT,

    new_value TEXT,

    ip_address VARCHAR(255),

    user_agent VARCHAR(1000),

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_admin_audit_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE RESTRICT
);

-- =====================================================
-- ENTITY INDEXES
-- (Matches @Table(indexes = ...))
-- =====================================================

CREATE INDEX idx_audit_admin
    ON admin_audit_logs(admin_id);

CREATE INDEX idx_audit_action
    ON admin_audit_logs(action);

CREATE INDEX idx_audit_created_at
    ON admin_audit_logs(created_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_audit_module
    ON admin_audit_logs(module);

CREATE INDEX idx_audit_entity_type
    ON admin_audit_logs(entity_type);

CREATE INDEX idx_audit_entity_id
    ON admin_audit_logs(entity_id);

CREATE INDEX idx_audit_ip
    ON admin_audit_logs(ip_address);

CREATE INDEX idx_audit_module_action
    ON admin_audit_logs(module, action);

CREATE INDEX idx_audit_entity
    ON admin_audit_logs(entity_type, entity_id);

CREATE INDEX idx_audit_admin_created
    ON admin_audit_logs(admin_id, created_at);

CREATE INDEX idx_audit_module_created
    ON admin_audit_logs(module, created_at);

CREATE INDEX idx_audit_action_created
    ON admin_audit_logs(action, created_at);