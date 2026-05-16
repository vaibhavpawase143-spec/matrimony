-- V10__create_education_levels.sql

CREATE TABLE education_levels (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_education_levels_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_education_levels_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_education_level_name 
ON education_levels(name);

CREATE INDEX idx_education_level_active 
ON education_levels(is_active);

CREATE INDEX idx_education_levels_admin_id 
ON education_levels(admin_id);