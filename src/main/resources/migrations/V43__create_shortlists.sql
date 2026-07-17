-- =====================================================
-- V43__create_shortlists.sql
-- Shortlists Table
-- =====================================================

CREATE TABLE shortlists (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER & PROFILE
    -- =====================================================
    user_id BIGINT NOT NULL,

    profile_id BIGINT NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
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

    CONSTRAINT uk_shortlist_user_profile
        UNIQUE (user_id, profile_id),

    CONSTRAINT fk_shortlist_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_shortlist_profile
        FOREIGN KEY (profile_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_shortlist_user
    ON shortlists(user_id);

CREATE INDEX idx_shortlist_profile
    ON shortlists(profile_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_shortlist_active
    ON shortlists(is_active);

CREATE INDEX idx_shortlist_deleted
    ON shortlists(is_deleted);

CREATE INDEX idx_shortlist_created_at
    ON shortlists(created_at);

CREATE INDEX idx_shortlist_user_active
    ON shortlists(user_id, is_active);

CREATE INDEX idx_shortlist_profile_active
    ON shortlists(profile_id, is_active);

CREATE INDEX idx_shortlist_user_profile_active
    ON shortlists(user_id, profile_id, is_active);

CREATE INDEX idx_shortlist_user_created
    ON shortlists(user_id, created_at);

CREATE INDEX idx_shortlist_profile_created
    ON shortlists(profile_id, created_at);