-- =====================================================
-- V13__create_qualifications.sql
-- Qualification Master Table
-- =====================================================

CREATE TABLE qualifications
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_qualification_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_qualification_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_qualification_name
    ON qualifications(name);

CREATE INDEX idx_qualification_active
    ON qualifications(is_active);

CREATE INDEX idx_qualification_deleted
    ON qualifications(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_qualification_admin
    ON qualifications(admin_id);

CREATE INDEX idx_qualification_created_at
    ON qualifications(created_at);