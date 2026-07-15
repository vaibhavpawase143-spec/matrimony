-- V9__create_drinking.sql

CREATE TABLE drinking (
    id BIGSERIAL PRIMARY KEY,

    value VARCHAR(100) NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_drinking_value_admin 
        UNIQUE (value, admin_id),

    CONSTRAINT fk_drinking_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_drinking_admin 
ON drinking(admin_id);

CREATE INDEX idx_drinking_active 
ON drinking(is_active);