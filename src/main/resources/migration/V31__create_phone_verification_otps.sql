-- Phone Verification OTP Table
CREATE TABLE IF NOT EXISTS phone_verification_otps (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(15) NOT NULL,
    otp VARCHAR(6) NOT NULL,
    verified BOOLEAN DEFAULT false,
    expiry_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempt_count INTEGER DEFAULT 0
);

CREATE INDEX idx_phone_verification_phone ON phone_verification_otps(phone);
CREATE INDEX idx_phone_verification_created_at ON phone_verification_otps(created_at);

