-- =====================================================
-- V36__create_genders.sql
-- Gender Master Table
-- =====================================================

CREATE TABLE genders
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(20) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_gender_name
        UNIQUE (name)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_gender_name
    ON genders(name);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_gender_active
    ON genders(is_active);

CREATE INDEX idx_gender_created_at
    ON genders(created_at);