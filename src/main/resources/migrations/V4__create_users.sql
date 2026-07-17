CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,

    -- ==========================================
    -- AUDIT (Inherited from Auditable)
    -- ==========================================
    created_at TIMESTAMP NOT NULL,
    created_by BIGINT,

    updated_at TIMESTAMP,
    updated_by BIGINT,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by BIGINT,

    deletion_reason VARCHAR(500),

    version BIGINT DEFAULT 0,

    -- ==========================================
    -- BASIC INFORMATION
    -- ==========================================
    first_name VARCHAR(255),
    middle_name VARCHAR(255),
    last_name VARCHAR(255),

    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),

    password VARCHAR(255) NOT NULL,

    -- ==========================================
    -- ACCOUNT STATUS
    -- ==========================================
    is_active BOOLEAN DEFAULT TRUE,

    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,

    email_verified_at TIMESTAMP,
    phone_verified_at TIMESTAMP,

    -- ==========================================
    -- OTP
    -- ==========================================
    otp VARCHAR(255),
    otp_expiry TIMESTAMP,

    -- ==========================================
    -- USER ACTIVITY
    -- ==========================================
    is_online BOOLEAN DEFAULT FALSE,

    last_seen TIMESTAMP,
    last_login TIMESTAMP,
    last_heartbeat TIMESTAMP,

    -- ==========================================
    -- SECURITY
    -- ==========================================
    is_blocked BOOLEAN DEFAULT FALSE,

    report_count INTEGER DEFAULT 0,

    -- ==========================================
    -- CONSTRAINTS
    -- ==========================================

    CONSTRAINT uk_users_email UNIQUE(email),
    CONSTRAINT uk_users_phone UNIQUE(phone)
);

-- ==========================================
-- INDEXES
-- ==========================================

CREATE INDEX idx_users_email
ON users(email);

CREATE INDEX idx_users_phone
ON users(phone);

CREATE INDEX idx_users_active
ON users(is_active);

CREATE INDEX idx_users_deleted
ON users(is_deleted);

CREATE INDEX idx_users_online
ON users(is_online);

CREATE INDEX idx_users_last_login
ON users(last_login);

CREATE INDEX idx_users_created_at
ON users(created_at);