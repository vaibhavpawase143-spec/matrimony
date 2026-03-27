-- V40__create_partner_preferences.sql

CREATE TABLE partner_preferences (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    min_age INT,
    max_age INT,

    min_height DOUBLE PRECISION,
    max_height DOUBLE PRECISION,

    religion_id BIGINT,
    caste_id BIGINT,
    city_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_partner_preferences_user 
        UNIQUE (user_id),

    CONSTRAINT fk_partner_preferences_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_partner_preferences_religion
        FOREIGN KEY (religion_id)
        REFERENCES religions(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_partner_preferences_caste
        FOREIGN KEY (caste_id)
        REFERENCES castes(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_partner_preferences_city
        FOREIGN KEY (city_id)
        REFERENCES cities(id)
        ON DELETE SET NULL,

    -- 🔥 VALIDATIONS (VERY IMPORTANT)
    CONSTRAINT chk_age_range 
        CHECK (min_age IS NULL OR max_age IS NULL OR min_age <= max_age),

    CONSTRAINT chk_height_range 
        CHECK (min_height IS NULL OR max_height IS NULL OR min_height <= max_height)
);

-- ================= INDEXES =================

CREATE INDEX idx_pref_user 
ON partner_preferences(user_id);

CREATE INDEX idx_pref_religion_id 
ON partner_preferences(religion_id);

CREATE INDEX idx_pref_caste_id 
ON partner_preferences(caste_id);

CREATE INDEX idx_pref_city_id 
ON partner_preferences(city_id);