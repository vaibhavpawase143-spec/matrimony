-- =====================================================
-- V26__create_family_status.sql
-- Family Status Master Table
-- =====================================================

CREATE TABLE family_status
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

    CONSTRAINT uk_family_status_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_family_status_name
    ON family_status(name);

CREATE INDEX idx_family_status_active
    ON family_status(is_active);

CREATE INDEX idx_family_status_deleted_at
    ON family_status(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_family_status_admin
    ON family_status(admin_id);

CREATE INDEX idx_family_status_created_at
    ON family_status(created_at);