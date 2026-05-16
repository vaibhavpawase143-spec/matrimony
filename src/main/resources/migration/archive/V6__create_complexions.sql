-- V6__create_complexions.sql

CREATE TABLE complexions (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_complexion_admin_value 
        UNIQUE (admin_id, value),

    CONSTRAINT fk_complexions_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_complexion_admin 
ON complexions(admin_id);

CREATE INDEX idx_complexion_active 
ON complexions(is_active);