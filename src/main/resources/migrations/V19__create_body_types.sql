-- =====================================================
-- V19__create_body_types.sql
-- Body Type Master Table
-- =====================================================

CREATE TABLE body_types
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_body_type_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_body_type_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_body_type_value
    ON body_types(value);

CREATE INDEX idx_body_type_active
    ON body_types(is_active);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_body_type_admin
    ON body_types(admin_id);

CREATE INDEX idx_body_type_deleted
    ON body_types(deleted_at);

CREATE INDEX idx_body_type_created_at
    ON body_types(created_at);