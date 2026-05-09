-- V31__create_photo_types.sql

CREATE TABLE photo_types (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_photo_types_name UNIQUE (name)
);

-- ✅ Index (optional but good)
CREATE INDEX idx_photo_types_name ON photo_types(name);