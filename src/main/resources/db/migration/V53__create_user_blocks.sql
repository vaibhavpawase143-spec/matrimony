-- =====================================================
-- V52__create_user_blocks.sql
-- User Blocks Table
-- =====================================================

CREATE TABLE user_blocks (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    blocker_id BIGINT NOT NULL,

    blocked_id BIGINT NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_user_blocks
        UNIQUE (blocker_id, blocked_id),

    CONSTRAINT fk_user_block_blocker
        FOREIGN KEY (blocker_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_block_blocked
        FOREIGN KEY (blocked_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_user_block_self
        CHECK (blocker_id <> blocked_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_user_block_blocker
    ON user_blocks(blocker_id);

CREATE INDEX idx_user_block_blocked
    ON user_blocks(blocked_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_user_block_active
    ON user_blocks(is_active);

CREATE INDEX idx_user_block_created
    ON user_blocks(created_at);

CREATE INDEX idx_user_block_updated
    ON user_blocks(updated_at);

CREATE INDEX idx_user_block_blocker_active
    ON user_blocks(blocker_id, is_active);

CREATE INDEX idx_user_block_blocked_active
    ON user_blocks(blocked_id, is_active);

CREATE INDEX idx_user_block_blocker_created
    ON user_blocks(blocker_id, created_at);

CREATE INDEX idx_user_block_blocked_created
    ON user_blocks(blocked_id, created_at);