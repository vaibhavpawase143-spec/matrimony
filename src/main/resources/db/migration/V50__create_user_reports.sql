-- =====================================================
-- V49__create_user_reports.sql
-- User Reports Table
-- =====================================================

CREATE TABLE user_reports (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USERS
    -- =====================================================
    reporter_id BIGINT NOT NULL,

    reported_user_id BIGINT NOT NULL,

    reviewed_by BIGINT,

    -- =====================================================
    -- REPORT DETAILS
    -- =====================================================
    reason VARCHAR(255),

    description TEXT,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    -- =====================================================
    -- DATES
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    reviewed_at TIMESTAMP WITHOUT TIME ZONE,

    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT fk_user_report_reporter
        FOREIGN KEY (reporter_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_report_reported_user
        FOREIGN KEY (reported_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_report_reviewed_by
        FOREIGN KEY (reviewed_by)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_user_report_status
        CHECK (
            status IN (
                'PENDING',
                'IN_PROGRESS',
                'RESOLVED',
                'CLOSED'
            )
        ),

    CONSTRAINT chk_user_report_users
        CHECK (
            reporter_id <> reported_user_id
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_user_report_reporter
    ON user_reports(reporter_id);

CREATE INDEX idx_user_report_reported_user
    ON user_reports(reported_user_id);

CREATE INDEX idx_user_report_status
    ON user_reports(status);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_user_report_created
    ON user_reports(created_at);

CREATE INDEX idx_user_report_updated
    ON user_reports(updated_at);

CREATE INDEX idx_user_report_reviewed_at
    ON user_reports(reviewed_at);

CREATE INDEX idx_user_report_reviewed_by
    ON user_reports(reviewed_by);

CREATE INDEX idx_user_report_reporter_status
    ON user_reports(reporter_id, status);

CREATE INDEX idx_user_report_reported_status
    ON user_reports(reported_user_id, status);

CREATE INDEX idx_user_report_reviewed_status
    ON user_reports(reviewed_by, status);

CREATE INDEX idx_user_report_created_status
    ON user_reports(created_at, status);