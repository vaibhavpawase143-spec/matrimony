-- =====================================================
-- V11__create_sub_castes.sql
-- Sub Caste Master Table
-- =====================================================

CREATE TABLE sub_castes
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    caste_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

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
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_subcaste_name
    ON sub_castes(name);

CREATE INDEX idx_subcaste_caste
    ON sub_castes(caste_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_subcaste_admin
    ON sub_castes(admin_id);

CREATE INDEX idx_subcaste_active
    ON sub_castes(is_active);

CREATE INDEX idx_subcaste_created_at
    ON sub_castes(created_at);