-- V38__create_profiles.sql

CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL UNIQUE,

    religion_id BIGINT,
    caste_id BIGINT,
    education_level_id BIGINT,
    occupation_id BIGINT,
    height_id BIGINT,
    weight_id BIGINT,
    city_id BIGINT,

    about VARCHAR(1000),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= FOREIGN KEYS =================

    CONSTRAINT fk_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_profiles_religion
        FOREIGN KEY (religion_id)
        REFERENCES religions(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_caste
        FOREIGN KEY (caste_id)
        REFERENCES castes(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_education_level
        FOREIGN KEY (education_level_id)
        REFERENCES education_levels(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_occupation
        FOREIGN KEY (occupation_id)
        REFERENCES occupations(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_height
        FOREIGN KEY (height_id)
        REFERENCES heights(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_weight
        FOREIGN KEY (weight_id)
        REFERENCES weights(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_city
        FOREIGN KEY (city_id)
        REFERENCES cities(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_profile_user ON profiles(user_id);
CREATE INDEX idx_profile_city ON profiles(city_id);
CREATE INDEX idx_profile_caste ON profiles(caste_id);
CREATE INDEX idx_profile_religion ON profiles(religion_id);
CREATE INDEX idx_profile_occupation ON profiles(occupation_id);

-- 🔥 ADVANCED MATCHING INDEX
CREATE INDEX idx_profile_search
ON profiles(religion_id, caste_id, city_id);