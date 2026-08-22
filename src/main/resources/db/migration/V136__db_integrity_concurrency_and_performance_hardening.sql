-- =========================================================================
-- V136__db_integrity_concurrency_and_performance_hardening.sql
-- Gathbandhan Platform - Database Integrity, Concurrency & Performance Hardening
-- Remediates: VULN-DB-01, VULN-DB-02, VULN-DB-04
-- =========================================================================

-- =========================================================================
-- 1. VULN-DB-01: ACTIVE SUBSCRIPTION CONCURRENCY HARDENING
-- Reconcile duplicate active subscriptions safely by keeping the latest ACTIVE
-- record and setting older duplicate active records to EXPIRED (zero row deletion).
-- =========================================================================

WITH ranked_subscriptions AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY user_id
               ORDER BY created_at DESC, id DESC
           ) AS rn
    FROM user_subscriptions
    WHERE is_active = true AND status = 'ACTIVE'
)
UPDATE user_subscriptions
SET is_active = false,
    status = 'EXPIRED',
    updated_at = CURRENT_TIMESTAMP
WHERE id IN (
    SELECT id FROM ranked_subscriptions WHERE rn > 1
);

-- Partial unique index ensuring at most ONE active subscription per user at DB engine level
CREATE UNIQUE INDEX IF NOT EXISTS uq_active_user_subscription
ON user_subscriptions (user_id)
WHERE is_active = true AND status = 'ACTIVE';

-- =========================================================================
-- 2. VULN-DB-02: PRIMARY PHOTO CONCURRENCY HARDENING
-- Reconcile duplicate primary photos safely by keeping the latest primary photo
-- and unsetting is_primary on older duplicate photos (zero photo deletion).
-- =========================================================================

WITH ranked_photos AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY user_id
               ORDER BY created_at DESC, id DESC
           ) AS rn
    FROM user_photos
    WHERE is_primary = true AND is_deleted = false
)
UPDATE user_photos
SET is_primary = false,
    updated_at = CURRENT_TIMESTAMP
WHERE id IN (
    SELECT id FROM ranked_photos WHERE rn > 1
);

-- Partial unique index ensuring at most ONE non-deleted primary photo per user at DB engine level
CREATE UNIQUE INDEX IF NOT EXISTS uq_primary_user_photo
ON user_photos (user_id)
WHERE is_primary = true AND is_deleted = false;

-- =========================================================================
-- 3. VULN-DB-04: PARTIAL INDEX PERFORMANCE OPTIMIZATION FOR ACTIVE QUERIES
-- Optimized partial indexes on high-frequency tables where queries filter by is_deleted = false
-- =========================================================================

-- Users active & not deleted lookup
CREATE INDEX IF NOT EXISTS idx_users_active_not_deleted
ON users (id, is_active)
WHERE is_deleted = false;

-- Users unique email lookup for non-deleted accounts
CREATE INDEX IF NOT EXISTS idx_users_email_not_deleted
ON users (email)
WHERE is_deleted = false;

-- Users unique phone lookup for non-deleted accounts
CREATE INDEX IF NOT EXISTS idx_users_phone_not_deleted
ON users (phone)
WHERE is_deleted = false AND phone IS NOT NULL;

-- Profiles active lookup for non-deleted profiles
CREATE INDEX IF NOT EXISTS idx_profiles_user_not_deleted
ON profiles (user_id)
WHERE is_deleted = false;

-- Interests active & not deleted lookups for sender/receiver queries
CREATE INDEX IF NOT EXISTS idx_interests_sender_active_not_deleted
ON interests (sender_id, status)
WHERE is_deleted = false AND is_active = true;

CREATE INDEX IF NOT EXISTS idx_interests_receiver_active_not_deleted
ON interests (receiver_id, status)
WHERE is_deleted = false AND is_active = true;
