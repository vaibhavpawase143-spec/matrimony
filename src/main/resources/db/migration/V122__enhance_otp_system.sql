-- =====================================================
-- V122__enhance_otp_system.sql
-- Enhance phone_verification_otps for Realtime OTP System
-- =====================================================

ALTER TABLE phone_verification_otps 
    ADD COLUMN IF NOT EXISTS target_type VARCHAR(20) DEFAULT 'PHONE',
    ADD COLUMN IF NOT EXISTS purpose VARCHAR(30) DEFAULT 'VERIFICATION',
    ADD COLUMN IF NOT EXISTS last_sent_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_phone_verification_target_type ON phone_verification_otps(target_type);
CREATE INDEX IF NOT EXISTS idx_phone_verification_purpose ON phone_verification_otps(purpose);
