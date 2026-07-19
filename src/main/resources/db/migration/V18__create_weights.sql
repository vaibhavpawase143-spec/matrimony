-- =====================================================
-- V18__create_weights.sql
-- Weight Master Table
-- =====================================================

CREATE TABLE weights
(
    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- ADMIN
    -- =====================================================
    admin_id BIGINT NOT NULL,

    -- =====================================================
    -- WEIGHT DETAILS
    -- =====================================================
    value VARCHAR(20) NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_weight_admin_value
        UNIQUE (admin_id, value),

    CONSTRAINT fk_weight_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_weight_value
    ON weights(value);

CREATE INDEX idx_weight_admin
    ON weights(admin_id);

CREATE INDEX idx_weight_active
    ON weights(is_active);

CREATE INDEX idx_weight_deleted
    ON weights(deleted_at);

CREATE INDEX idx_weight_created_at
    ON weights(created_at);