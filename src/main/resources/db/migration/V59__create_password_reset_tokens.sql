-- =====================================================
-- V58__create_password_reset_tokens.sql
-- Password Reset Tokens Table
-- =====================================================

CREATE TABLE password_reset_tokens (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- TOKEN
    -- =====================================================
    token VARCHAR(512) NOT NULL,

    user_id BIGINT NOT NULL,

    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_password_reset_token
        UNIQUE (token),

    CONSTRAINT uk_password_reset_user
        UNIQUE (user_id),

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_password_reset_user
    ON password_reset_tokens(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_password_reset_token
    ON password_reset_tokens(token);

CREATE INDEX idx_password_reset_expiry
    ON password_reset_tokens(expiry_date);

CREATE INDEX idx_password_reset_user_expiry
    ON password_reset_tokens(user_id, expiry_date);

CREATE INDEX idx_password_reset_token_expiry
    ON password_reset_tokens(token, expiry_date);