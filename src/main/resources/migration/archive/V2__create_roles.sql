-- V2__create_roles.sql

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_roles_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_roles_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 Prevent empty role names
    CONSTRAINT chk_roles_name_not_empty
        CHECK (name <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_role_name 
ON roles(name);

CREATE INDEX idx_roles_admin_id 
ON roles(admin_id);

-- 🔥 Composite index (important for lookups)
CREATE INDEX idx_roles_admin_name 
ON roles(admin_id, name);