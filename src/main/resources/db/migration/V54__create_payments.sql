-- =====================================================
-- V53__create_payments.sql
-- Payments Table
-- =====================================================

CREATE TABLE payments (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER
    -- =====================================================
    user_id BIGINT NOT NULL,

    -- =====================================================
    -- PAYMENT DETAILS
    -- =====================================================
    amount NUMERIC(10,2) NOT NULL,

    payment_method VARCHAR(50) NOT NULL,

    transaction_id VARCHAR(255) NOT NULL,

    plan_id BIGINT,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================


    CONSTRAINT uk_payment_transaction
        UNIQUE (transaction_id),

    CONSTRAINT fk_payment_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_payment_plan
        FOREIGN KEY (plan_id)
        REFERENCES subscription_plans(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_payment_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payment_status
        CHECK (
            status IN (
                'PENDING',
                'SUCCESS',
                'FAILED',
                'REFUNDED',
                'CANCELLED'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_payment_user
    ON payments(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_payment_transaction
    ON payments(transaction_id);

CREATE INDEX idx_payment_status
    ON payments(status);

CREATE INDEX idx_payment_created
    ON payments(created_at);

CREATE INDEX idx_payment_updated
    ON payments(updated_at);

CREATE INDEX idx_payment_plan
    ON payments(plan_id);

CREATE INDEX idx_payment_user_status
    ON payments(user_id, status);

CREATE INDEX idx_payment_user_created
    ON payments(user_id, created_at);

CREATE INDEX idx_payment_status_created
    ON payments(status, created_at);

CREATE INDEX idx_payment_method
    ON payments(payment_method);