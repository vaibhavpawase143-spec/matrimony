-- =====================================================
-- V30__create_manglik_status.sql
-- Manglik Status Master Table
-- =====================================================

CREATE TABLE manglik_status
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_manglik_status_admin_name
        UNIQUE (admin_id, name),

    CONSTRAINT fk_manglik_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_manglik_name
    ON manglik_status(name);

CREATE INDEX idx_manglik_active
    ON manglik_status(is_active);

CREATE INDEX idx_manglik_deleted
    ON manglik_status(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_manglik_admin
    ON manglik_status(admin_id);

CREATE INDEX idx_manglik_created_at
    ON manglik_status(created_at);
