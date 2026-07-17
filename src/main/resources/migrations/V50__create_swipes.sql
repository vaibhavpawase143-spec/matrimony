-- =====================================================
-- V50__create_swipes.sql
-- Swipes Table
-- =====================================================

CREATE TABLE swipes (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    from_user_id BIGINT NOT NULL,

    to_user_id BIGINT NOT NULL,

    -- =====================================================
    -- SWIPE
    -- =====================================================
    type VARCHAR(20) NOT NULL,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_swipe_users
        UNIQUE (from_user_id, to_user_id),

    CONSTRAINT fk_swipe_from_user
        FOREIGN KEY (from_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_swipe_to_user
        FOREIGN KEY (to_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_swipe_type
        CHECK (
            type IN (
                'LIKE',
                'DISLIKE',
                'PASS'
            )
        ),

    CONSTRAINT chk_swipe_users
        CHECK (
            from_user_id <> to_user_id
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_swipe_from
    ON swipes(from_user_id);

CREATE INDEX idx_swipe_to
    ON swipes(to_user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_swipe_created_at
    ON swipes(created_at);

CREATE INDEX idx_swipe_type
    ON swipes(type);

CREATE INDEX idx_swipe_from_created
    ON swipes(from_user_id, created_at);

CREATE INDEX idx_swipe_to_created
    ON swipes(to_user_id, created_at);

CREATE INDEX idx_swipe_from_type
    ON swipes(from_user_id, type);

CREATE INDEX idx_swipe_to_type
    ON swipes(to_user_id, type);

CREATE INDEX idx_swipe_created_type
    ON swipes(created_at, type);