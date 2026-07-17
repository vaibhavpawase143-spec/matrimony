-- =====================================================
-- V35__create_profile_types.sql
-- Profile Type Master Table
-- =====================================================

CREATE TABLE profile_types
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

    CONSTRAINT uk_profile_type_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_profile_type_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_profile_type_name
    ON profile_types(name);

CREATE INDEX idx_profile_type_active
    ON profile_types(is_active);

CREATE INDEX idx_profile_type_deleted
    ON profile_types(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_profile_type_admin
    ON profile_types(admin_id);

CREATE INDEX idx_profile_type_created_at
    ON profile_types(created_at);