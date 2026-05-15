-- V23__create_occupations.sql

CREATE TABLE occupations (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_occupations_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_occupations_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_occupation_name 
ON occupations(name);

CREATE INDEX idx_occupations_admin_id 
ON occupations(admin_id);