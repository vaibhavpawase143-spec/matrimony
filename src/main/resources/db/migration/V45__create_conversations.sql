-- =====================================================
-- V44__create_conversations.sql
-- Conversations Table
-- =====================================================

CREATE TABLE conversations (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- PARTICIPANTS
    -- =====================================================
    user1_id BIGINT NOT NULL,

    user2_id BIGINT NOT NULL,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    created_by BIGINT,

    updated_at TIMESTAMP WITHOUT TIME ZONE,

    updated_by BIGINT,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    deleted_by BIGINT,

    deletion_reason VARCHAR(500),

    version BIGINT NOT NULL DEFAULT 0,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_conversation_users
        UNIQUE (user1_id, user2_id),

    CONSTRAINT fk_conversation_user1
        FOREIGN KEY (user1_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_conversation_user2
        FOREIGN KEY (user2_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_conversation_users
        CHECK (user1_id <> user2_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_conversation_user1
    ON conversations(user1_id);

CREATE INDEX idx_conversation_user2
    ON conversations(user2_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_conversation_created_at
    ON conversations(created_at);

CREATE INDEX idx_conversation_updated_at
    ON conversations(updated_at);

CREATE INDEX idx_conversation_user1_created
    ON conversations(user1_id, created_at);

CREATE INDEX idx_conversation_user2_created
    ON conversations(user2_id, created_at);

CREATE INDEX idx_conversation_users
    ON conversations(user1_id, user2_id);

CREATE INDEX idx_conversation_is_deleted
    ON conversations(is_deleted);

CREATE INDEX idx_conversation_created_by
    ON conversations(created_by);

CREATE INDEX idx_conversation_updated_by
    ON conversations(updated_by);

CREATE INDEX idx_conversation_deleted_by
    ON conversations(deleted_by);

CREATE INDEX idx_conversation_active_users
    ON conversations(user1_id, user2_id, is_deleted);