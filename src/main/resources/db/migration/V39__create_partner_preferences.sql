-- =====================================================
-- V39__create_partner_preferences.sql
-- Partner Preferences Table
-- =====================================================

CREATE TABLE partner_preferences (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER
    -- =====================================================
    user_id BIGINT NOT NULL,

    -- =====================================================
    -- AGE
    -- =====================================================
    min_age INTEGER,
    max_age INTEGER,

    -- =====================================================
    -- EDUCATION
    -- =====================================================
    education_level_id BIGINT,

    occupation_id BIGINT,

    other_expectations VARCHAR(1000),

    -- =====================================================
    -- MARITAL
    -- =====================================================
    marital_status_id BIGINT,

    -- =====================================================
    -- LIFESTYLE
    -- =====================================================
    smoking_id BIGINT,

    drinking_id BIGINT,

    diet_id BIGINT,

    -- =====================================================
    -- HEIGHT & WEIGHT
    -- =====================================================
    min_height BIGINT,
    max_height BIGINT,

    min_weight BIGINT,
    max_weight BIGINT,

    -- =====================================================
    -- RELIGION
    -- =====================================================
    religion_id BIGINT,

    caste_id BIGINT,

    -- =====================================================
    -- LOCATION
    -- =====================================================
    city_id BIGINT,

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
    CONSTRAINT uk_partner_preferences_user UNIQUE (user_id),

    CONSTRAINT fk_partner_preferences_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_partner_preferences_education_level
        FOREIGN KEY (education_level_id)
        REFERENCES education_levels(id),

    CONSTRAINT fk_partner_preferences_occupation
        FOREIGN KEY (occupation_id)
        REFERENCES occupations(id),

    CONSTRAINT fk_partner_preferences_marital_status
        FOREIGN KEY (marital_status_id)
        REFERENCES marital_status(id),

    CONSTRAINT fk_partner_preferences_smoking
        FOREIGN KEY (smoking_id)
        REFERENCES smoking(id),

    CONSTRAINT fk_partner_preferences_drinking
        FOREIGN KEY (drinking_id)
        REFERENCES drinking(id),

    CONSTRAINT fk_partner_preferences_diet
        FOREIGN KEY (diet_id)
        REFERENCES diets(id),

    CONSTRAINT fk_partner_preferences_religion
        FOREIGN KEY (religion_id)
        REFERENCES religions(id),

    CONSTRAINT fk_partner_preferences_caste
        FOREIGN KEY (caste_id)
        REFERENCES castes(id),

    CONSTRAINT fk_partner_preferences_city
        FOREIGN KEY (city_id)
        REFERENCES cities(id)
);

-- =====================================================
-- ENTITY INDEXES
-- =====================================================

CREATE INDEX idx_pref_user
    ON partner_preferences(user_id);

CREATE INDEX idx_pref_religion
    ON partner_preferences(religion_id);

CREATE INDEX idx_pref_caste
    ON partner_preferences(caste_id);

CREATE INDEX idx_pref_city
    ON partner_preferences(city_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_partner_pref_education
    ON partner_preferences(education_level_id);

CREATE INDEX idx_partner_pref_occupation
    ON partner_preferences(occupation_id);

CREATE INDEX idx_partner_pref_marital
    ON partner_preferences(marital_status_id);

CREATE INDEX idx_partner_pref_smoking
    ON partner_preferences(smoking_id);

CREATE INDEX idx_partner_pref_drinking
    ON partner_preferences(drinking_id);

CREATE INDEX idx_partner_pref_diet
    ON partner_preferences(diet_id);

CREATE INDEX idx_partner_pref_active
    ON partner_preferences(is_active);

CREATE INDEX idx_partner_pref_deleted
    ON partner_preferences(is_deleted);

CREATE INDEX idx_partner_pref_created_at
    ON partner_preferences(created_at);