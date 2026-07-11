-- V5__create_brother_types.sql

CREATE TABLE brother_types (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uk_brother_type_admin_value 
        UNIQUE (admin_id, value),

    CONSTRAINT fk_brother_types_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_brother_type_value 
ON brother_types(value);

CREATE INDEX idx_brother_type_active 
ON brother_types(is_active);

CREATE INDEX idx_brother_types_admin_id 
ON brother_types(admin_id);