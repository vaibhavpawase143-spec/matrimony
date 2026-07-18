-- =====================================================
-- V55__create_refresh_tokens.sql
-- Refresh Tokens Table
-- =====================================================

CREATE TABLE refresh_token (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- TOKEN
    -- =====================================================
    token VARCHAR(512) NOT NULL,

    email VARCHAR(255) NOT NULL,

    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_refresh_token
        UNIQUE (token),

    CONSTRAINT uk_refresh_token_email
        UNIQUE (email)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_refresh_token_email
    ON refresh_token(email);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_refresh_token_expiry
    ON refresh_token(expiry_date);

CREATE INDEX idx_refresh_token_email_expiry
    ON refresh_token(email, expiry_date);

CREATE INDEX idx_refresh_token_token_expiry
    ON refresh_token(token, expiry_date);