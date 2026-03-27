-- V16__create_fields_of_study.sql

CREATE TABLE fields_of_study (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(255) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_fields_of_study_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_fields_of_study_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_field_of_study_name 
ON fields_of_study(name);

CREATE INDEX idx_fields_of_study_admin_id 
ON fields_of_study(admin_id);