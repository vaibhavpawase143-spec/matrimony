-- V27__create_sister_types.sql

CREATE TABLE sister_types (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_sister_types_admin_value 
        UNIQUE (admin_id, value),

    CONSTRAINT fk_sister_types_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 Prevent empty values
    CONSTRAINT chk_sister_types_value_not_empty
        CHECK (value <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_sister_type_value 
ON sister_types(value);

CREATE INDEX idx_sister_types_admin_id 
ON sister_types(admin_id);

-- 🔥 Composite index (important)
CREATE INDEX idx_sister_types_admin_value 
ON sister_types(admin_id, value);