-- ==========================================
-- USERS
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_users_active
ON users(is_active);

CREATE INDEX IF NOT EXISTS idx_users_deleted
ON users(is_deleted);

CREATE INDEX IF NOT EXISTS idx_users_blocked
ON users(is_blocked);

CREATE INDEX IF NOT EXISTS idx_users_created
ON users(created_at);

CREATE INDEX IF NOT EXISTS idx_users_active_deleted
ON users(is_active, is_deleted);

-- ==========================================
-- PROFILES
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_profile_user
ON profiles(user_id);

CREATE INDEX IF NOT EXISTS idx_profile_gender
ON profiles(gender_id);

CREATE INDEX IF NOT EXISTS idx_profile_city
ON profiles(city_id);

CREATE INDEX IF NOT EXISTS idx_profile_state
ON profiles(state_id);

CREATE INDEX IF NOT EXISTS idx_profile_country
ON profiles(country_id);

CREATE INDEX IF NOT EXISTS idx_profile_religion
ON profiles(religion_id);

CREATE INDEX IF NOT EXISTS idx_profile_caste
ON profiles(caste_id);

CREATE INDEX IF NOT EXISTS idx_profile_subcaste
ON profiles(sub_caste_id);

CREATE INDEX IF NOT EXISTS idx_profile_occupation
ON profiles(occupation_id);

CREATE INDEX IF NOT EXISTS idx_profile_education
ON profiles(education_level_id);

CREATE INDEX IF NOT EXISTS idx_profile_marital
ON profiles(marital_status_id);

CREATE INDEX IF NOT EXISTS idx_profile_active
ON profiles(is_active);

CREATE INDEX IF NOT EXISTS idx_profile_completed
ON profiles(profile_completed);

CREATE INDEX IF NOT EXISTS idx_profile_premium
ON profiles(is_premium);

CREATE INDEX IF NOT EXISTS idx_profile_created
ON profiles(created_at);

CREATE INDEX IF NOT EXISTS idx_profile_active_completed
ON profiles(is_active, profile_completed);

CREATE INDEX IF NOT EXISTS idx_profile_discover
ON profiles(is_active, profile_completed, is_premium);

-- ==========================================
-- MATCHES
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_match_user1
ON matches(user1_id);

CREATE INDEX IF NOT EXISTS idx_match_user2
ON matches(user2_id);

-- ==========================================
-- SWIPES
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_swipe_from
ON swipes(from_user_id);

CREATE INDEX IF NOT EXISTS idx_swipe_to
ON swipes(to_user_id);

CREATE INDEX IF NOT EXISTS idx_swipe_pair
ON swipes(from_user_id, to_user_id);

-- ==========================================
-- USER BLOCKS
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_block_blocker
ON user_blocks(blocker_id);

CREATE INDEX IF NOT EXISTS idx_block_blocked
ON user_blocks(blocked_id);

CREATE INDEX IF NOT EXISTS idx_block_active
ON user_blocks(is_active);

-- ==========================================
-- NOTIFICATIONS
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_notification_receiver
ON notifications(receiver_id);

CREATE INDEX IF NOT EXISTS idx_notification_read
ON notifications(read);

-- ==========================================
-- USER SUBSCRIPTIONS
-- ==========================================

CREATE INDEX IF NOT EXISTS idx_subscription_user
ON user_subscriptions(user_id);

CREATE INDEX IF NOT EXISTS idx_subscription_status
ON user_subscriptions(status);

CREATE INDEX IF NOT EXISTS idx_subscription_active
ON user_subscriptions(is_active);