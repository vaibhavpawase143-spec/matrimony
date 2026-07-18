-- =====================================================
-- V40__create_user_photos.sql
-- User Photos Table
-- =====================================================

CREATE TABLE user_photos (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER
    -- =====================================================
    user_id BIGINT NOT NULL,

    -- =====================================================
    -- PHOTO DETAILS
    -- =====================================================
    photo_type VARCHAR(50) NOT NULL,

    photo_url VARCHAR(1000) NOT NULL,

    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

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
    -- FOREIGN KEYS
    -- =====================================================

    CONSTRAINT fk_user_photos_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- =====================================================
    -- CHECK CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_user_photos_photo_type
        CHECK (
            photo_type IN (
                'PROFILE',
                'KUNDALI',
                'COVER',
                'OTHER'
            )
        )
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_user_photo_user
    ON user_photos(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_user_photos_type
    ON user_photos(photo_type);

CREATE INDEX idx_user_photos_primary
    ON user_photos(is_primary);

CREATE INDEX idx_user_photos_deleted
    ON user_photos(is_deleted);

CREATE INDEX idx_user_photos_created_at
    ON user_photos(created_at);

CREATE INDEX idx_user_photos_user_primary
    ON user_photos(user_id, is_primary);

CREATE INDEX idx_user_photos_user_type
    ON user_photos(user_id, photo_type);