-- V12__create_family.sql

CREATE TABLE family (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uk_family_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_family_admin 
ON family(admin_id);

CREATE INDEX idx_family_active 
ON family(is_active);