-- =====================================================
-- V14__create_fields_of_study.sql
-- Field Of Study Master Table
-- =====================================================

CREATE TABLE fields_of_study
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_field_of_study_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_field_of_study_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_field_of_study_name
    ON fields_of_study(name);

CREATE INDEX idx_field_of_study_active
    ON fields_of_study(is_active);

CREATE INDEX idx_field_of_study_deleted_at
    ON fields_of_study(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_field_of_study_admin
    ON fields_of_study(admin_id);

CREATE INDEX idx_field_of_study_created_at
    ON fields_of_study(created_at);