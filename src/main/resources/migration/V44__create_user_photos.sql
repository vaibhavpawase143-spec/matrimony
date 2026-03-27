-- V44__create_user_photos.sql

CREATE TABLE user_photos (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    photo_type VARCHAR(50) NOT NULL,

    photo_url VARCHAR(500) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraint
    CONSTRAINT uq_user_photos_user_type
        UNIQUE (user_id, photo_type),

    -- ✅ Foreign key
    CONSTRAINT fk_user_photos_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- ✅ Enum constraint (FIXED)
    CONSTRAINT chk_user_photos_type
        CHECK (photo_type IN ('PROFILE', 'KUNDALI', 'COVER', 'OTHER'))
);

-- ✅ Index
CREATE INDEX idx_user_photo_user ON user_photos(user_id);

-- 🔥 Optional composite index
CREATE INDEX idx_user_photo_user_type 
ON user_photos(user_id, photo_type);