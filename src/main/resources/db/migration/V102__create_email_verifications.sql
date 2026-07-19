-- =====================================================
-- V102__create_email_verifications.sql
-- Email Verifications Table
-- =====================================================

CREATE TABLE email_verifications (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- VERIFICATION DATA
    -- =====================================================
    email VARCHAR(255) NOT NULL,

    token VARCHAR(255) NOT NULL UNIQUE,

    verified BOOLEAN NOT NULL DEFAULT FALSE,

    expiry_date TIMESTAMP WITHOUT TIME ZONE
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_email_verification_email
    ON email_verifications(email);

CREATE INDEX idx_email_verification_token
    ON email_verifications(token);

CREATE INDEX idx_email_verification_verified
    ON email_verifications(verified);

CREATE INDEX idx_email_verification_expiry
    ON email_verifications(expiry_date);