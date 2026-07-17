-- =====================================================
-- V45__create_messages.sql
-- Messages Table
-- =====================================================

CREATE TABLE messages (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- RELATIONS
    -- =====================================================
    conversation_id BIGINT NOT NULL,

    sender_id BIGINT NOT NULL,

    reply_to_id BIGINT,

    -- =====================================================
    -- MESSAGE
    -- =====================================================
    content VARCHAR(1000),

    message_type VARCHAR(50) NOT NULL DEFAULT 'TEXT',

    status VARCHAR(20) NOT NULL DEFAULT 'SENT',

    -- =====================================================
    -- MEDIA
    -- =====================================================
    media_url VARCHAR(1000),

    media_type VARCHAR(50),

    -- =====================================================
    -- REACTION
    -- =====================================================
    reaction VARCHAR(100),

    pinned BOOLEAN NOT NULL DEFAULT FALSE,

    starred BOOLEAN NOT NULL DEFAULT FALSE,

    -- =====================================================
    -- DELETE
    -- =====================================================
    deleted_for_everyone BOOLEAN NOT NULL DEFAULT FALSE,

    deleted_for_users VARCHAR(500),

    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- =====================================================
    -- DATES
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    seen_at TIMESTAMP WITHOUT TIME ZONE,

    -- =====================================================
    -- FOREIGN KEYS
    -- =====================================================

    CONSTRAINT fk_messages_conversation
        FOREIGN KEY (conversation_id)
        REFERENCES conversations(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_messages_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_messages_reply
        FOREIGN KEY (reply_to_id)
        REFERENCES messages(id)
        ON DELETE SET NULL,

    -- =====================================================
    -- CHECK CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_message_status
        CHECK (
            status IN (
                'SENT',
                'DELIVERED',
                'SEEN'
            )
        ),

    CONSTRAINT chk_message_type
        CHECK (
            message_type IN (
                'TEXT',
                'IMAGE',
                'VIDEO',
                'AUDIO',
                'DOCUMENT'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_messages_conversation
    ON messages(conversation_id);

CREATE INDEX idx_messages_sender
    ON messages(sender_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_messages_created_at
    ON messages(created_at);

CREATE INDEX idx_messages_status
    ON messages(status);

CREATE INDEX idx_messages_seen_at
    ON messages(seen_at);

CREATE INDEX idx_messages_deleted
    ON messages(is_deleted);

CREATE INDEX idx_messages_reply
    ON messages(reply_to_id);

CREATE INDEX idx_messages_message_type
    ON messages(message_type);

CREATE INDEX idx_messages_conversation_created
    ON messages(conversation_id, created_at);

CREATE INDEX idx_messages_conversation_status
    ON messages(conversation_id, status);

CREATE INDEX idx_messages_sender_created
    ON messages(sender_id, created_at);

CREATE INDEX idx_messages_conversation_deleted
    ON messages(conversation_id, is_deleted);

CREATE INDEX idx_messages_pinned
    ON messages(pinned);

CREATE INDEX idx_messages_starred
    ON messages(starred);