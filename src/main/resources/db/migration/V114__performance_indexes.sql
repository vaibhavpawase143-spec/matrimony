-- ==========================================
-- PERFORMANCE INDEXES FOR MATCHING
-- ==========================================

-- Used by findCandidateUsers()
CREATE INDEX IF NOT EXISTS idx_users_match_status
ON users(is_active, is_deleted, is_blocked);

CREATE INDEX IF NOT EXISTS idx_profiles_match
ON profiles(
    gender_id,
    is_active,
    profile_completed
);

CREATE INDEX IF NOT EXISTS idx_profiles_match_sort
ON profiles(
    is_premium DESC,
    boost_score DESC
);

CREATE INDEX IF NOT EXISTS idx_users_match_created
ON users(created_at DESC);

-- Used for blocked users lookup
CREATE INDEX IF NOT EXISTS idx_user_blocks_pair
ON user_blocks(blocker_id, blocked_id);

-- Used for shortlist
CREATE INDEX IF NOT EXISTS idx_shortlists_user_profile
ON shortlists(user_id, profile_id);

-- Used for interests
CREATE INDEX IF NOT EXISTS idx_interests_sender_receiver
ON interests(sender_id, receiver_id);

-- Used for conversations
CREATE INDEX IF NOT EXISTS idx_conversations_users
ON conversations(user1_id, user2_id);

-- Used for messages
CREATE INDEX IF NOT EXISTS idx_messages_conversation_created
ON messages(conversation_id, created_at DESC);