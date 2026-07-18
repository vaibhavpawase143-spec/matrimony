-- =====================================================
-- V27__create_family_types.sql
-- Family Type Master Table
-- =====================================================

CREATE TABLE family_types
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

    CONSTRAINT uk_family_type_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_type_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_family_type_name
    ON family_types(name);

CREATE INDEX idx_family_type_active
    ON family_types(is_active);

CREATE INDEX idx_family_type_deleted_at
    ON family_types(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_family_type_admin
    ON family_types(admin_id);

CREATE INDEX idx_family_type_created_at
    ON family_types(created_at);