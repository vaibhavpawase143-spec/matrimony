-- =====================================================
-- V33__create_sister_types.sql
-- Sister Type Master Table
-- =====================================================

CREATE TABLE sister_types
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    value VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_sister_type_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_sister_type_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_sister_type_value
    ON sister_types(value);

CREATE INDEX idx_sister_type_active
    ON sister_types(is_active);

CREATE INDEX idx_sister_type_deleted
    ON sister_types(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_sister_type_admin
    ON sister_types(admin_id);

CREATE INDEX idx_sister_type_created_at
    ON sister_types(created_at);