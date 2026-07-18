CREATE TABLE admins (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    username VARCHAR(255) NOT NULL UNIQUE,

    email VARCHAR(255) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    profile_photo VARCHAR(255),

    role_id BIGINT,

    phone VARCHAR(20),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    last_login TIMESTAMP,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_by BIGINT,

    deleted_at TIMESTAMP
);

-- ===========================
-- Indexes
-- ===========================

CREATE INDEX idx_admin_email
ON admins(email);

CREATE INDEX idx_admin_username
ON admins(username);