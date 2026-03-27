-- V15__create_family_values.sql

CREATE TABLE family_values (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    admin_id BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_family_values_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_values_admin
        FOREIGN KEY (admin_id) 
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_family_value_name 
ON family_values(name);

CREATE INDEX idx_family_values_admin_id 
ON family_values(admin_id);