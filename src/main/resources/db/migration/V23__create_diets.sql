-- =====================================================
-- V23__create_diets.sql
-- Diet Master Table
-- =====================================================

CREATE TABLE diets
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_diet_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_diet_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_diet_admin
    ON diets(admin_id);

CREATE INDEX idx_diet_active
    ON diets(is_active);

CREATE INDEX idx_diet_deleted
    ON diets(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_diet_name
    ON diets(name);

CREATE INDEX idx_diet_created_at
    ON diets(created_at);