-- V43__create_shortlists.sql

CREATE TABLE shortlists (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= FOREIGN KEYS =================

    CONSTRAINT fk_shortlists_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_shortlists_profile
        FOREIGN KEY (profile_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    -- 🔥 Prevent self-shortlisting (VERY IMPORTANT)
    CONSTRAINT chk_shortlist_not_self
        CHECK (user_id <> profile_id)
);

-- ================= UNIQUE (SOFT DELETE SAFE) =================

CREATE UNIQUE INDEX uq_shortlists_active
ON shortlists(user_id, profile_id)
WHERE is_active = true;

-- ================= INDEXES =================

CREATE INDEX idx_shortlist_user ON shortlists(user_id);
CREATE INDEX idx_shortlist_profile ON shortlists(profile_id);
CREATE INDEX idx_shortlist_active ON shortlists(is_active);

-- 🔥 Composite index for fast queries
CREATE INDEX idx_shortlist_user_active
ON shortlists(user_id, is_active);