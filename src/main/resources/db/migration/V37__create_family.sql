-- =====================================================
-- V37__create_family.sql
-- Family Master Table
-- =====================================================

CREATE TABLE family
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

    CONSTRAINT uk_family_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_family_name
    ON family(name);

CREATE INDEX idx_family_active
    ON family(is_active);

CREATE INDEX idx_family_deleted_at
    ON family(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_family_admin
    ON family(admin_id);

CREATE INDEX idx_family_created_at
    ON family(created_at);