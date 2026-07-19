-- =====================================================
-- V11__create_sub_castes.sql
-- Sub Caste Master Table
-- =====================================================

CREATE TABLE sub_castes
(
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- RELATIONSHIPS
    -- =====================================================

    admin_id BIGINT NOT NULL,

    caste_id BIGINT NOT NULL,

    -- =====================================================
    -- BASIC DETAILS
    -- =====================================================

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT FIELDS
    -- =====================================================

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    -- =====================================================
    -- SOFT DELETE
    -- =====================================================

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_subcaste_caste_name
        UNIQUE (caste_id, name),

    CONSTRAINT fk_subcaste_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT fk_subcaste_caste
        FOREIGN KEY (caste_id)
        REFERENCES castes(id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_subcaste_name
    ON sub_castes(name);

CREATE INDEX idx_subcaste_caste
    ON sub_castes(caste_id);

CREATE INDEX idx_subcaste_admin
    ON sub_castes(admin_id);

CREATE INDEX idx_subcaste_active
    ON sub_castes(is_active);

CREATE INDEX idx_subcaste_deleted
    ON sub_castes(deleted_at);

CREATE INDEX idx_subcaste_created_at
    ON sub_castes(created_at);