-- =====================================================
-- V25__create_smoking.sql
-- Smoking Master Table
-- =====================================================

CREATE TABLE smoking
(
    id BIGSERIAL PRIMARY KEY,

    value VARCHAR(50) NOT NULL,

    admin_id BIGINT NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_smoking_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_smoking_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_smoking_admin
    ON smoking(admin_id);

CREATE INDEX idx_smoking_value
    ON smoking(value);

CREATE INDEX idx_smoking_active
    ON smoking(is_active);

CREATE INDEX idx_smoking_deleted
    ON smoking(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_smoking_created_at
    ON smoking(created_at);