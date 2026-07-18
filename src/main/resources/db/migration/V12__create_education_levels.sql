-- =====================================================
-- V12__create_education_levels.sql
-- Education Level Master Table
-- =====================================================

CREATE TABLE education_levels
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

    CONSTRAINT uk_education_level_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_education_level_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_education_level_name
    ON education_levels(name);

CREATE INDEX idx_education_level_active
    ON education_levels(is_active);

CREATE INDEX idx_education_level_deleted_at
    ON education_levels(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_education_level_admin
    ON education_levels(admin_id);

CREATE INDEX idx_education_level_created_at
    ON education_levels(created_at);