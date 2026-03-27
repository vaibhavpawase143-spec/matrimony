-- V1__create_admins.sql

CREATE TABLE admins (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    username VARCHAR(255) NOT NULL UNIQUE,

    email VARCHAR(255) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    role_id BIGINT,

    phone VARCHAR(20),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    last_login TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_admin_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_admin_email ON admins(email);

CREATE INDEX idx_admin_username ON admins(username);

CREATE INDEX idx_admin_role_id ON admins(role_id);