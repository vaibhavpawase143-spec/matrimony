-- =====================================================
-- V17__create_heights.sql
-- Height Master Table
-- =====================================================

CREATE TABLE heights
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    height VARCHAR(20) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_height_admin_value
        UNIQUE (height, admin_id),

    CONSTRAINT fk_height_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_height_value
    ON heights(height);

CREATE INDEX idx_height_active
    ON heights(is_active);

CREATE INDEX idx_height_deleted_at
    ON heights(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_height_admin
    ON heights(admin_id);

CREATE INDEX idx_height_created_at
    ON heights(created_at);