-- =====================================================
-- V48__create_reports.sql
-- Reports Table
-- =====================================================

CREATE TABLE reports (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    reported_by_id BIGINT NOT NULL,

    reported_user_id BIGINT NOT NULL,

    assigned_admin_id BIGINT,

    -- =====================================================
    -- REPORT DETAILS
    -- =====================================================
    reason TEXT NOT NULL,

    description TEXT,

    report_type VARCHAR(50) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    admin_notes TEXT,

    resolution_notes TEXT,

    -- =====================================================
    -- DATES
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    resolved_at TIMESTAMP WITHOUT TIME ZONE,

    -- =====================================================
    -- FOREIGN KEYS
    -- =====================================================

    CONSTRAINT fk_report_reported_by
        FOREIGN KEY (reported_by_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_report_reported_user
        FOREIGN KEY (reported_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_report_assigned_admin
        FOREIGN KEY (assigned_admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- =====================================================
    -- CHECK CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_report_type
        CHECK (
            report_type IN (
                'INAPPROPRIATE_PROFILE',
                'INAPPROPRIATE_MESSAGE',
                'FAKE_PROFILE',
                'HARASSMENT',
                'SPAM',
                'SCAM',
                'OTHER'
            )
        ),

    CONSTRAINT chk_report_status
        CHECK (
            status IN (
                'PENDING',
                'IN_PROGRESS',
                'RESOLVED',
                'CLOSED'
            )
        ),

    CONSTRAINT chk_report_users
        CHECK (reported_by_id <> reported_user_id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_report_user
    ON reports(reported_user_id);

CREATE INDEX idx_report_admin
    ON reports(assigned_admin_id);

CREATE INDEX idx_report_status
    ON reports(status);

CREATE INDEX idx_report_created_at
    ON reports(created_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_report_reported_by
    ON reports(reported_by_id);

CREATE INDEX idx_report_report_type
    ON reports(report_type);

CREATE INDEX idx_report_updated_at
    ON reports(updated_at);

CREATE INDEX idx_report_resolved_at
    ON reports(resolved_at);

CREATE INDEX idx_report_user_status
    ON reports(reported_user_id, status);

CREATE INDEX idx_report_admin_status
    ON reports(assigned_admin_id, status);

CREATE INDEX idx_report_created_status
    ON reports(created_at, status);

CREATE INDEX idx_report_type_status
    ON reports(report_type, status);

CREATE INDEX idx_report_reported_by_created
    ON reports(reported_by_id, created_at);

CREATE INDEX idx_report_reported_user_created
    ON reports(reported_user_id, created_at);