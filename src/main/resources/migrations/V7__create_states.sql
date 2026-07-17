-- =====================================================
-- V7__create_states.sql
-- State Master Table
-- =====================================================

CREATE TABLE states
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    country_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_state_country_name
        UNIQUE (country_id, name),

    CONSTRAINT fk_state_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT fk_state_country
        FOREIGN KEY (country_id)
        REFERENCES countries(id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_state_name
    ON states(name);

CREATE INDEX idx_state_country
    ON states(country_id);

CREATE INDEX idx_state_active
    ON states(is_active);

CREATE INDEX idx_state_deleted
    ON states(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_state_admin
    ON states(admin_id);

CREATE INDEX idx_state_created_at
    ON states(created_at);