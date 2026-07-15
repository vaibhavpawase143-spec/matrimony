-- V14__create_family_types.sql

CREATE TABLE family_types (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uk_family_type_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_types_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_family_type_name 
ON family_types(name);

CREATE INDEX idx_family_type_active 
ON family_types(is_active);

CREATE INDEX idx_family_types_admin_id 
ON family_types(admin_id);