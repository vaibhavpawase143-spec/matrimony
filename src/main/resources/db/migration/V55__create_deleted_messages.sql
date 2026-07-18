-- =====================================================
-- V54__create_deleted_messages.sql
-- Deleted Messages Table
-- =====================================================

CREATE TABLE deleted_messages (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- REFERENCES
    -- =====================================================
    message_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    deleted_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_deleted_message_message
        FOREIGN KEY (message_id)
        REFERENCES messages(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_deleted_message_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_deleted_message_user
        UNIQUE (message_id, user_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_deleted_message_message
    ON deleted_messages(message_id);

CREATE INDEX idx_deleted_message_user
    ON deleted_messages(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_deleted_message_deleted_at
    ON deleted_messages(deleted_at);

CREATE INDEX idx_deleted_message_user_deleted
    ON deleted_messages(user_id, deleted_at);

CREATE INDEX idx_deleted_message_message_deleted
    ON deleted_messages(message_id, deleted_at);

CREATE INDEX idx_deleted_message_user_message
    ON deleted_messages(user_id, message_id);