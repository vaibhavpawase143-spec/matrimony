-- =====================================================
-- V15__create_occupations.sql
-- Occupation Master Table
-- =====================================================

CREATE TABLE occupations
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

    CONSTRAINT uk_occupation_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_occupation_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_occupation_name
    ON occupations(name);

CREATE INDEX idx_occupation_active
    ON occupations(is_active);

CREATE INDEX idx_occupation_deleted
    ON occupations(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_occupation_admin
    ON occupations(admin_id);

CREATE INDEX idx_occupation_created_at
    ON occupations(created_at);