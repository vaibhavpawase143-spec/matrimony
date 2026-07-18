-- =====================================================
-- V22__create_mother_tongues.sql
-- Mother Tongue Master Table
-- =====================================================

CREATE TABLE mother_tongues
(
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_mother_tongue_name_admin
        UNIQUE (name, admin_id),

    CONSTRAINT fk_mother_tongue_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_mother_tongue_name
    ON mother_tongues(name);

CREATE INDEX idx_mother_tongue_active
    ON mother_tongues(is_active);

CREATE INDEX idx_mother_tongue_deleted
    ON mother_tongues(deleted_at);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_mother_tongue_admin
    ON mother_tongues(admin_id);

CREATE INDEX idx_mother_tongue_created_at
    ON mother_tongues(created_at);