-- V24__create_profile_types.sql

CREATE TABLE profile_types (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_profile_types_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_profile_types_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 Prevent empty names
    CONSTRAINT chk_profile_types_name_not_empty
        CHECK (name <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_profile_type_name 
ON profile_types(name);

CREATE INDEX idx_profile_types_admin_id 
ON profile_types(admin_id);

-- 🔥 Composite index (better filtering)
CREATE INDEX idx_profile_types_admin_name 
ON profile_types(admin_id, name);