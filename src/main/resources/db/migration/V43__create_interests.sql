-- =====================================================
-- V42__create_interests.sql
-- Interests Table
-- =====================================================

CREATE TABLE interests (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    sender_id BIGINT NOT NULL,

    receiver_id BIGINT NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT (Inherited from Auditable)
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

    CONSTRAINT uk_interest_sender_receiver
        UNIQUE (sender_id, receiver_id),

    CONSTRAINT fk_interest_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_interest_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_interest_status
        CHECK (
            status IN (
                'PENDING',
                'ACCEPTED',
                'REJECTED'
            )
        ),

    CONSTRAINT chk_interest_not_self
        CHECK (sender_id <> receiver_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_interest_sender
    ON interests(sender_id);

CREATE INDEX idx_interest_receiver
    ON interests(receiver_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_interest_status
    ON interests(status);

CREATE INDEX idx_interest_active
    ON interests(is_active);

CREATE INDEX idx_interest_deleted
    ON interests(is_deleted);

CREATE INDEX idx_interest_created_at
    ON interests(created_at);

CREATE INDEX idx_interest_sender_status
    ON interests(sender_id, status);

CREATE INDEX idx_interest_receiver_status
    ON interests(receiver_id, status);

CREATE INDEX idx_interest_sender_active
    ON interests(sender_id, is_active);

CREATE INDEX idx_interest_receiver_active
    ON interests(receiver_id, is_active);

CREATE INDEX idx_interest_sender_receiver_status
    ON interests(sender_id, receiver_id, status);