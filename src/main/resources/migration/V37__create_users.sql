-- V37__create_users.sql

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL,
    phone VARCHAR(15) NOT NULL,

    password VARCHAR(255) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    email_verified_at TIMESTAMP,
    phone_verified_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraints
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_phone UNIQUE (phone)
);

-- ✅ Indexes
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_phone ON users(phone);

-- 🔥 Optional (for verification queries)
CREATE INDEX idx_user_email_verified ON users(email_verified_at);
CREATE INDEX idx_user_phone_verified ON users(phone_verified_at);