-- =====================================================
-- V57__create_phone_verification_otps.sql
-- Phone Verification OTPs Table
-- =====================================================

CREATE TABLE phone_verification_otps (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- PHONE VERIFICATION
    -- =====================================================
    phone VARCHAR(20) NOT NULL,

    otp VARCHAR(10) NOT NULL,

    verified BOOLEAN NOT NULL DEFAULT FALSE,

    expiry_date TIMESTAMP WITHOUT TIME ZONE,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    attempt_count INTEGER NOT NULL DEFAULT 0,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_phone_verification_attempts
        CHECK (attempt_count >= 0 AND attempt_count <= 5)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_phone_verification_phone
    ON phone_verification_otps(phone);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_phone_verification_otp
    ON phone_verification_otps(otp);

CREATE INDEX idx_phone_verification_verified
    ON phone_verification_otps(verified);

CREATE INDEX idx_phone_verification_expiry
    ON phone_verification_otps(expiry_date);

CREATE INDEX idx_phone_verification_created
    ON phone_verification_otps(created_at);

CREATE INDEX idx_phone_verification_phone_verified
    ON phone_verification_otps(phone, verified);

CREATE INDEX idx_phone_verification_phone_expiry
    ON phone_verification_otps(phone, expiry_date);

CREATE INDEX idx_phone_verification_verified_expiry
    ON phone_verification_otps(verified, expiry_date);

CREATE INDEX idx_phone_verification_attempt_count
    ON phone_verification_otps(attempt_count);