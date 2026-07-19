-- =====================================================
-- V21__create_marital_status.sql
-- Marital Status Master Table
-- =====================================================

CREATE TABLE marital_status
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_marital_status_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_marital_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_marital_status_name
    ON marital_status(name);

CREATE INDEX idx_marital_status_active
    ON marital_status(is_active);

CREATE INDEX idx_marital_status_deleted
    ON marital_status(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_marital_status_admin
    ON marital_status(admin_id);

CREATE INDEX idx_marital_status_created_at
    ON marital_status(created_at);