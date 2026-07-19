-- =====================================================
-- V56__create_verification_tokens.sql
-- Verification Tokens Table
-- =====================================================

CREATE TABLE verification_tokens (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- TOKEN
    -- =====================================================
    token VARCHAR(512) NOT NULL,

    email VARCHAR(255) NOT NULL,

    user_id BIGINT NOT NULL,

    verified BOOLEAN NOT NULL DEFAULT FALSE,

    expiry_date TIMESTAMP WITHOUT TIME ZONE,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_verification_token
        UNIQUE (token),

    CONSTRAINT uk_verification_user
        UNIQUE (user_id),

    CONSTRAINT fk_verification_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_verification_email
    ON verification_tokens(email);

CREATE INDEX idx_verification_user
    ON verification_tokens(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_verification_expiry
    ON verification_tokens(expiry_date);

CREATE INDEX idx_verification_created
    ON verification_tokens(created_at);

CREATE INDEX idx_verification_verified
    ON verification_tokens(verified);

CREATE INDEX idx_verification_email_verified
    ON verification_tokens(email, verified);

CREATE INDEX idx_verification_user_verified
    ON verification_tokens(user_id, verified);

CREATE INDEX idx_verification_expiry_verified
    ON verification_tokens(expiry_date, verified);