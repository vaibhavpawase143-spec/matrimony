-- =====================================================
-- V28__create_family_values.sql
-- Family Value Master Table
-- =====================================================

CREATE TABLE family_values
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

    CONSTRAINT uk_family_value_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_value_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_family_value_name
    ON family_values(name);

CREATE INDEX idx_family_value_active
    ON family_values(is_active);

CREATE INDEX idx_family_value_deleted_at
    ON family_values(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_family_value_admin
    ON family_values(admin_id);

CREATE INDEX idx_family_value_created_at
    ON family_values(created_at);