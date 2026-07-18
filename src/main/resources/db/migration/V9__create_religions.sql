-- =====================================================
-- V9__create_religions.sql
-- Religion Master Table
-- =====================================================

CREATE TABLE religions
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_religion_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_religion_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_religion_name
    ON religions(name);

CREATE INDEX idx_religion_active
    ON religions(is_active);

CREATE INDEX idx_religion_deleted
    ON religions(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_religion_admin
    ON religions(admin_id);

CREATE INDEX idx_religion_created_at
    ON religions(created_at);