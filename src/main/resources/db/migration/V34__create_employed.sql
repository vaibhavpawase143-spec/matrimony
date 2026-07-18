-- =====================================================
-- V34__create_employed.sql
-- Employed Master Table
-- =====================================================

CREATE TABLE employed
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

    CONSTRAINT uk_employed_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_employed_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_employed_name
    ON employed(name);

CREATE INDEX idx_employed_active
    ON employed(is_active);

CREATE INDEX idx_employed_deleted_at
    ON employed(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_employed_admin
    ON employed(admin_id);

CREATE INDEX idx_employed_created_at
    ON employed(created_at);