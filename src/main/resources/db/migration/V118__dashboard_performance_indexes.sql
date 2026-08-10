-- ============================================================
-- DASHBOARD PERFORMANCE INDEXES
-- ============================================================

-- Users
CREATE INDEX IF NOT EXISTS idx_users_created_at
ON users(created_at);

CREATE INDEX IF NOT EXISTS idx_users_active_deleted
ON users(is_active, is_deleted);

CREATE INDEX IF NOT EXISTS idx_users_verified_deleted
ON users(email_verified, phone_verified, is_deleted);

CREATE INDEX IF NOT EXISTS idx_users_blocked_deleted
ON users(is_blocked, is_deleted);

-- Profiles
CREATE INDEX IF NOT EXISTS idx_profiles_city
ON profiles(city_id);

CREATE INDEX IF NOT EXISTS idx_profiles_religion
ON profiles(religion_id);

CREATE INDEX IF NOT EXISTS idx_profiles_created
ON profiles(created_at);

-- Payments
CREATE INDEX IF NOT EXISTS idx_payments_status_created
ON payments(status, created_at);

CREATE INDEX IF NOT EXISTS idx_payments_plan
ON payments(plan_id);

-- Reports
CREATE INDEX IF NOT EXISTS idx_reports_status_created
ON reports(status, created_at);

-- Subscriptions
CREATE INDEX IF NOT EXISTS idx_user_subscriptions_status
ON user_subscriptions(status);

CREATE INDEX IF NOT EXISTS idx_user_subscriptions_active
ON user_subscriptions(is_active);

CREATE INDEX IF NOT EXISTS idx_user_subscriptions_created
ON user_subscriptions(created_at);