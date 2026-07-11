-- V19__create_interests.sql

CREATE TABLE interests (
    id BIGSERIAL PRIMARY KEY,

    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT uq_interest_sender_receiver 
        UNIQUE (sender_id, receiver_id),

    CONSTRAINT fk_interest_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_interest_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- 🔥 IMPORTANT VALIDATIONS
    CONSTRAINT chk_interest_status 
        CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),

    CONSTRAINT chk_no_self_interest
        CHECK (sender_id <> receiver_id)
);

-- ================= INDEXES =================

CREATE INDEX idx_interest_sender 
ON interests(sender_id);

CREATE INDEX idx_interest_receiver 
ON interests(receiver_id);

-- 🔥 Composite index for faster queries
CREATE INDEX idx_interest_sender_status 
ON interests(sender_id, status);

CREATE INDEX idx_interest_receiver_status 
ON interests(receiver_id, status);