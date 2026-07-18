-- =====================================================
-- V8__create_cities.sql
-- City Master Table
-- =====================================================

CREATE TABLE cities
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    state_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_city_state_name
        UNIQUE (state_id, name),

    CONSTRAINT fk_city_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT fk_city_state
        FOREIGN KEY (state_id)
        REFERENCES states(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_city_name
    ON cities(name);

CREATE INDEX idx_city_state
    ON cities(state_id);

CREATE INDEX idx_city_active
    ON cities(is_active);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_city_deleted
    ON cities(deleted_at);

CREATE INDEX idx_city_admin
    ON cities(admin_id);

CREATE INDEX idx_city_created_at
    ON cities(created_at);