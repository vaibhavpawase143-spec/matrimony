-- =====================================================
-- V51__create_profile_visitors.sql
-- Profile Visitors Table
-- =====================================================

CREATE TABLE profile_visitors (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    visitor_id BIGINT NOT NULL,

    visited_user_id BIGINT NOT NULL,

    -- =====================================================
    -- VISIT DETAILS
    -- =====================================================
    viewed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_profile_visitor
        FOREIGN KEY (visitor_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_profile_visited_user
        FOREIGN KEY (visited_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_profile_visitor_users
        CHECK (visitor_id <> visited_user_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_profile_visitor
    ON profile_visitors(visitor_id);

CREATE INDEX idx_profile_visited_user
    ON profile_visitors(visited_user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_profile_viewed_at
    ON profile_visitors(viewed_at);

CREATE INDEX idx_profile_visitor_viewed
    ON profile_visitors(visitor_id, viewed_at);

CREATE INDEX idx_profile_visited_viewed
    ON profile_visitors(visited_user_id, viewed_at);

CREATE INDEX idx_profile_visitor_visited
    ON profile_visitors(visitor_id, visited_user_id);

CREATE INDEX idx_profile_visited_visitor
    ON profile_visitors(visited_user_id, visitor_id);