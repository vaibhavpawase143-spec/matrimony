-- V3__create_blood_groups.sql

CREATE TABLE blood_groups (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    type VARCHAR(5) NOT NULL UNIQUE,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_blood_groups_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_blood_group ON blood_groups(type);

CREATE INDEX idx_blood_group_active ON blood_groups(is_active);

CREATE INDEX idx_blood_groups_admin_id ON blood_groups(admin_id);