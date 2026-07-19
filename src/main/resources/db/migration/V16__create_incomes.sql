-- =====================================================
-- V16__create_incomes.sql
-- Income Master Table
-- =====================================================

CREATE TABLE incomes
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    range VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_income_admin_range
        UNIQUE (admin_id, range),

    CONSTRAINT fk_income_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_income_range
    ON incomes(range);

CREATE INDEX idx_income_active
    ON incomes(is_active);

CREATE INDEX idx_income_deleted
    ON incomes(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_income_admin
    ON incomes(admin_id);

CREATE INDEX idx_income_created_at
    ON incomes(created_at);