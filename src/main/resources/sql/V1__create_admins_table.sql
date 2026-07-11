CREATE TABLE admins (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    username VARCHAR(100) NOT NULL UNIQUE,

    email VARCHAR(150) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    role_id BIGINT,

    phone VARCHAR(20),

    isActive BOOLEAN DEFAULT TRUE,

    last_login TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_admin_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);

-- Indexes
CREATE INDEX idx_admin_email ON admins(email);
CREATE INDEX idx_admin_username ON admins(username);