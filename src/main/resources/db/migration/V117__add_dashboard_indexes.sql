-- ==========================================
-- DASHBOARD PERFORMANCE INDEXES
-- ==========================================

-- Profiles
CREATE INDEX IF NOT EXISTS idx_profiles_city
ON profiles(city_id);

CREATE INDEX IF NOT EXISTS idx_profiles_religion
ON profiles(religion_id);

-- Users
CREATE INDEX IF NOT EXISTS idx_users_created_at
ON users(created_at);

CREATE INDEX IF NOT EXISTS idx_users_is_deleted_created_at
ON users(is_deleted, created_at);

-- Payments
CREATE INDEX IF NOT EXISTS idx_payments_status_created_at
ON payments(status, created_at);

CREATE INDEX IF NOT EXISTS idx_payments_plan_status
ON payments(plan_id, status);

-- Reports
CREATE INDEX IF NOT EXISTS idx_user_reports_created_at
ON user_reports(created_at);

CREATE INDEX IF NOT EXISTS idx_user_reports_status
ON user_reports(status);

-- Subscriptions
CREATE INDEX IF NOT EXISTS idx_user_subscriptions_status
ON user_subscriptions(status);

CREATE INDEX IF NOT EXISTS idx_user_subscriptions_is_active
ON user_subscriptions(is_active);

-- Update PostgreSQL statistics
ANALYZE users;
ANALYZE profiles;
ANALYZE payments;
ANALYZE user_reports;
ANALYZE user_subscriptions;