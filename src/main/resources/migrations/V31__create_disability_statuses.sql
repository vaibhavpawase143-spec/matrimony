-- =====================================================
-- V31__create_disability_statuses.sql
-- Disability Status Master Table
-- =====================================================

CREATE TABLE disability_statuses
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_disability_status_value_admin
        UNIQUE (value, admin_id),

    CONSTRAINT fk_disability_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_disability_admin
    ON disability_statuses(admin_id);

CREATE INDEX idx_disability_active
    ON disability_statuses(is_active);

CREATE INDEX idx_disability_deleted
    ON disability_statuses(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_disability_value
    ON disability_statuses(value);

CREATE INDEX idx_disability_created_at
    ON disability_statuses(created_at);