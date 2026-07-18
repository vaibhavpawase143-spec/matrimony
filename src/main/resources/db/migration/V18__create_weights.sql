-- =====================================================
-- V18__create_weights.sql
-- Weight Master Table
-- =====================================================

CREATE TABLE weights
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(20) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

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
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_weight_value
    ON weights(value);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_weight_admin
    ON weights(admin_id);

CREATE INDEX idx_weight_active
    ON weights(is_active);

CREATE INDEX idx_weight_created_at
    ON weights(created_at);