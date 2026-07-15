-- V42__create_payments.sql

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    amount NUMERIC(10, 2) NOT NULL,

    payment_method VARCHAR(50) NOT NULL,

    transaction_id VARCHAR(255) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_payments_transaction_id 
        UNIQUE (transaction_id),

    CONSTRAINT fk_payments_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_payment_status
        CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),

    -- 🔥 IMPORTANT VALIDATION
    CONSTRAINT chk_payment_amount_positive
        CHECK (amount > 0)
);

-- ================= INDEXES =================

CREATE INDEX idx_payment_user 
ON payments(user_id);

CREATE INDEX idx_payment_status 
ON payments(status);

CREATE INDEX idx_payment_created_at 
ON payments(created_at);

-- 🔥 Composite index for queries
CREATE INDEX idx_payment_user_status 
ON payments(user_id, status);