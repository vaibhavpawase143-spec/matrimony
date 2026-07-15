-- V4__create_body_types.sql

CREATE TABLE body_types (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_body_types_value UNIQUE (value),

    CONSTRAINT fk_body_types_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_body_type_active ON body_types(is_active);

CREATE INDEX idx_body_types_admin_id ON body_types(admin_id);