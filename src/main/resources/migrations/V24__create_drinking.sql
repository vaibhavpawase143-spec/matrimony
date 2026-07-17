-- =====================================================
-- V24__create_drinking.sql
-- Drinking Master Table
-- =====================================================

CREATE TABLE drinking
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    value VARCHAR(100) NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_drinking_value_admin
        UNIQUE (value, admin_id),

    CONSTRAINT fk_drinking_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_drinking_admin
    ON drinking(admin_id);

CREATE INDEX idx_drinking_active
    ON drinking(is_active);

CREATE INDEX idx_drinking_deleted
    ON drinking(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_drinking_name
    ON drinking(name);

CREATE INDEX idx_drinking_value
    ON drinking(value);

CREATE INDEX idx_drinking_created_at
    ON drinking(created_at);