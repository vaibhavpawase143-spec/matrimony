-- V11__create_employed.sql

CREATE TABLE employed (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uk_employed_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_employed_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_employed_name 
ON employed(name);

CREATE INDEX idx_employed_active 
ON employed(is_active);

CREATE INDEX idx_employed_admin_id 
ON employed(admin_id);