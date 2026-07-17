CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    CONSTRAINT uk_role_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_role_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- ==========================================
-- INDEXES
-- ==========================================

CREATE INDEX idx_role_name
    ON roles(name);

CREATE INDEX idx_role_active
    ON roles(is_active);

CREATE INDEX idx_role_deleted
    ON roles(deleted_at);