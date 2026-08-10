CREATE INDEX IF NOT EXISTS idx_profile_gender
    ON profiles(gender_id);

CREATE INDEX IF NOT EXISTS idx_profile_completed
    ON profiles(profile_completed);

CREATE INDEX IF NOT EXISTS idx_profile_premium
    ON profiles(is_premium);

CREATE INDEX IF NOT EXISTS idx_profile_boost
    ON profiles(boost_score);

CREATE INDEX IF NOT EXISTS idx_user_status
    ON users(is_active, is_deleted, is_blocked);