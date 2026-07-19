-- =====================================================
-- V46__create_notifications.sql
-- Notifications Table
-- =====================================================

CREATE TABLE notifications (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    sender_id BIGINT,

    receiver_id BIGINT NOT NULL,

    matched_user_id BIGINT,

    match_percentage INTEGER,

    -- =====================================================
    -- CONTENT
    -- =====================================================
    title VARCHAR(255),

    message TEXT NOT NULL,

    type VARCHAR(30) NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
    read BOOLEAN NOT NULL DEFAULT FALSE,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- =====================================================
    -- DATES
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- FOREIGN KEYS
    -- =====================================================

    CONSTRAINT fk_notification_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_notification_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_notification_matched_user
        FOREIGN KEY (matched_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    -- =====================================================
    -- CHECK CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_notification_type
        CHECK (
            type IN (
                'REQUEST',
                'VIEW',
                'MESSAGE',
                'SHORTLIST',
                'ACCEPT',
                'REJECT',
                'LIKE',
                'MATCH',
                'ANNOUNCEMENT',
                'SYSTEM',
                'MAINTENANCE',
                'SUBSCRIPTION',
                'WARNING'
            )
        ),

    CONSTRAINT chk_match_percentage
        CHECK (
            match_percentage IS NULL
            OR (match_percentage >= 0 AND match_percentage <= 100)
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_notification_sender
    ON notifications(sender_id);

CREATE INDEX idx_notification_receiver
    ON notifications(receiver_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_notification_created_at
    ON notifications(created_at);

CREATE INDEX idx_notification_type
    ON notifications(type);

CREATE INDEX idx_notification_read
    ON notifications(read);

CREATE INDEX idx_notification_deleted
    ON notifications(deleted);

CREATE INDEX idx_notification_receiver_read
    ON notifications(receiver_id, read);

CREATE INDEX idx_notification_receiver_deleted
    ON notifications(receiver_id, deleted);

CREATE INDEX idx_notification_receiver_created
    ON notifications(receiver_id, created_at);

CREATE INDEX idx_notification_sender_created
    ON notifications(sender_id, created_at);

CREATE INDEX idx_notification_matched_user
    ON notifications(matched_user_id);