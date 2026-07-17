-- =====================================================
-- V47__create_support_tickets.sql
-- Support Tickets Table
-- =====================================================

CREATE TABLE support_tickets (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- TICKET DETAILS
    -- =====================================================
    ticket_number VARCHAR(100) NOT NULL,

    user_id BIGINT,

    category VARCHAR(30) NOT NULL,

    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',

    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    subject VARCHAR(255) NOT NULL,

    message TEXT,

    attachment_url VARCHAR(1000),

    admin_reply TEXT,

    assigned_admin_id BIGINT,

    resolved_at TIMESTAMP WITHOUT TIME ZONE,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_support_ticket_number
        UNIQUE (ticket_number),

    CONSTRAINT fk_support_ticket_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_support_ticket_admin
        FOREIGN KEY (assigned_admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_support_category
        CHECK (
            category IN (
                'ACCOUNT',
                'PROFILE',
                'PHOTO',
                'PREMIUM',
                'PAYMENT',
                'CHAT',
                'MATCH',
                'REPORT',
                'TECHNICAL',
                'FEATURE_REQUEST',
                'OTHER'
            )
        ),

    CONSTRAINT chk_support_priority
        CHECK (
            priority IN (
                'LOW',
                'MEDIUM',
                'HIGH'
            )
        ),

    CONSTRAINT chk_support_status
        CHECK (
            status IN (
                'OPEN',
                'IN_PROGRESS',
                'RESOLVED',
                'CLOSED'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_support_ticket_user
    ON support_tickets(user_id);

CREATE INDEX idx_support_ticket_admin
    ON support_tickets(assigned_admin_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_support_ticket_ticket_number
    ON support_tickets(ticket_number);

CREATE INDEX idx_support_ticket_status
    ON support_tickets(status);

CREATE INDEX idx_support_ticket_priority
    ON support_tickets(priority);

CREATE INDEX idx_support_ticket_category
    ON support_tickets(category);

CREATE INDEX idx_support_ticket_created
    ON support_tickets(created_at);

CREATE INDEX idx_support_ticket_updated
    ON support_tickets(updated_at);

CREATE INDEX idx_support_ticket_resolved
    ON support_tickets(resolved_at);

CREATE INDEX idx_support_ticket_user_status
    ON support_tickets(user_id, status);

CREATE INDEX idx_support_ticket_admin_status
    ON support_tickets(assigned_admin_id, status);

CREATE INDEX idx_support_ticket_status_priority
    ON support_tickets(status, priority);

CREATE INDEX idx_support_ticket_created_status
    ON support_tickets(created_at, status);

CREATE INDEX idx_support_ticket_user_created
    ON support_tickets(user_id, created_at);

CREATE INDEX idx_support_ticket_admin_created
    ON support_tickets(assigned_admin_id, created_at);