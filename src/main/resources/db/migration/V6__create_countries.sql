-- =====================================================
-- V6__create_countries.sql
-- Country Master Table
-- =====================================================

CREATE TABLE countries
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_country_name
        UNIQUE (name),

    CONSTRAINT fk_country_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_country_name
    ON countries(name);

CREATE INDEX idx_country_active
    ON countries(is_active);

CREATE INDEX idx_country_deleted
    ON countries(deleted_at);

CREATE INDEX idx_country_admin
    ON countries(admin_id);

CREATE INDEX idx_country_created_at
    ON countries(created_at);