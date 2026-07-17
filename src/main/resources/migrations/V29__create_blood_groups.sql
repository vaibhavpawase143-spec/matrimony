-- =====================================================
-- V29__create_blood_groups.sql
-- Blood Group Master Table
-- =====================================================

CREATE TABLE blood_groups
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    type VARCHAR(5) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_blood_group_type
        UNIQUE (type),

    CONSTRAINT fk_blood_group_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_blood_group_admin
    ON blood_groups(admin_id);

CREATE INDEX idx_blood_group_active
    ON blood_groups(is_active);

CREATE INDEX idx_blood_group_deleted
    ON blood_groups(deleted_at);

CREATE INDEX idx_blood_group_created_at
    ON blood_groups(created_at);