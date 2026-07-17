-- =====================================================
-- V32__create_brother_types.sql
-- Brother Type Master Table
-- =====================================================

CREATE TABLE brother_types
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

    CONSTRAINT uk_brother_type_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_brother_type_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_brother_type_value
    ON brother_types(value);

CREATE INDEX idx_brother_type_active
    ON brother_types(is_active);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_brother_type_admin
    ON brother_types(admin_id);

CREATE INDEX idx_brother_type_deleted
    ON brother_types(deleted_at);

CREATE INDEX idx_brother_type_created_at
    ON brother_types(created_at);