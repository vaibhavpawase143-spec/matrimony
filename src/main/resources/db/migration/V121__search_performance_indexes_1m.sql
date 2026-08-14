-- ============================================================
-- HIGH-PERFORMANCE COMPOSITE INDEXES FOR 1M USERS SEARCH & NOTIFICATIONS
-- ============================================================

-- Profiles Composite Search Index
CREATE INDEX IF NOT EXISTS idx_profiles_search_composite
ON profiles(religion_id, caste_id, city_id, date_of_birth);

CREATE INDEX IF NOT EXISTS idx_profiles_dob_gender
ON profiles(gender_id, date_of_birth);

CREATE INDEX IF NOT EXISTS idx_profiles_education_occupation
ON profiles(education_level_id, occupation_id);

CREATE INDEX IF NOT EXISTS idx_profiles_marital_status
ON profiles(marital_status_id);

-- Notifications Index for fast unread & list fetching
CREATE INDEX IF NOT EXISTS idx_notifications_receiver_status
ON notifications(receiver_id, deleted, read, created_at DESC);

-- User Blocks Composite Index for search filtering
CREATE INDEX IF NOT EXISTS idx_user_blocks_composite
ON user_blocks(blocker_id, blocked_id);
