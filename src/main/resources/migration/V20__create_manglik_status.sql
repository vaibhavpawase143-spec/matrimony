-- V20__create_manglik_status.sql

CREATE TABLE manglik_status (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_manglik_status_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_manglik_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_manglik_name 
ON manglik_status(name);

CREATE INDEX idx_manglik_status_admin_id 
ON manglik_status(admin_id);