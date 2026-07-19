-- =====================================================
-- V104__create_matches.sql
-- Matches Table
-- =====================================================

CREATE TABLE matches
(
    id BIGSERIAL PRIMARY KEY,

    user1_id BIGINT NOT NULL,

    user2_id BIGINT NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_matches_users
        UNIQUE (user1_id, user2_id),

    CONSTRAINT fk_matches_user1
        FOREIGN KEY (user1_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_matches_user2
        FOREIGN KEY (user2_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_matches_different_users
        CHECK (user1_id <> user2_id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_match_user1
    ON matches(user1_id);

CREATE INDEX idx_match_user2
    ON matches(user2_id);

CREATE INDEX idx_match_created_at
    ON matches(created_at);