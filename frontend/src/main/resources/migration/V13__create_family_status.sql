-- V13__create_family_status.sql

CREATE TABLE family_status (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uk_family_status_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_family_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_family_status_admin 
ON family_status(admin_id);

CREATE INDEX idx_family_status_active 
ON family_status(is_active);