-- =====================================================
-- V10__create_castes.sql
-- Caste Master Table
-- =====================================================

CREATE TABLE castes
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    religion_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_caste_admin_religion_name
        UNIQUE (admin_id, religion_id, name),

    CONSTRAINT fk_caste_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT fk_caste_religion
        FOREIGN KEY (religion_id)
        REFERENCES religions(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_caste_name
    ON castes(name);

CREATE INDEX idx_caste_religion
    ON castes(religion_id);

CREATE INDEX idx_caste_active
    ON castes(is_active);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_caste_admin
    ON castes(admin_id);

CREATE INDEX idx_caste_deleted
    ON castes(deleted_at);

CREATE INDEX idx_caste_created_at
    ON castes(created_at);