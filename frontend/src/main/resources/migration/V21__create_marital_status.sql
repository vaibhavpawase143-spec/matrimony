-- V21__create_marital_status.sql

CREATE TABLE marital_status (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_marital_status_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_marital_status_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_marital_status_name 
ON marital_status(name);

CREATE INDEX idx_marital_status_admin_id 
ON marital_status(admin_id);