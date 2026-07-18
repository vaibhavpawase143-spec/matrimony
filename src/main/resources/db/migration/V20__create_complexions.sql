-- =====================================================
-- V20__create_complexions.sql
-- Complexion Master Table
-- =====================================================

CREATE TABLE complexions
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_complexion_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_complexion_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_complexion_admin
    ON complexions(admin_id);

CREATE INDEX idx_complexion_active
    ON complexions(is_active);

CREATE INDEX idx_complexion_deleted
    ON complexions(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_complexion_value
    ON complexions(value);

CREATE INDEX idx_complexion_created_at
    ON complexions(created_at);