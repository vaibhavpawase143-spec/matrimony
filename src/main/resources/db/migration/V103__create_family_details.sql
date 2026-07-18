-- =====================================================
-- V103__create_family_details.sql
-- Family Details Table
-- =====================================================

CREATE TABLE family_details (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- RELATIONSHIPS
    -- =====================================================
    profile_id BIGINT NOT NULL,

    family_type_id BIGINT,

    family_id BIGINT,

    brother_type_id BIGINT,

    sister_type_id BIGINT,

    -- =====================================================
    -- FAMILY INFORMATION
    -- =====================================================
    father_occupation VARCHAR(150),

    mother_occupation VARCHAR(150),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITHOUT TIME ZONE,

    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================
    CONSTRAINT uk_family_details_profile
        UNIQUE(profile_id),

    CONSTRAINT fk_family_details_profile
        FOREIGN KEY(profile_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_family_details_family_type
        FOREIGN KEY(family_type_id)
        REFERENCES family_types(id),

  CONSTRAINT fk_family_details_family
      FOREIGN KEY (family_id)
      REFERENCES family(id),

    CONSTRAINT fk_family_details_brother_type
        FOREIGN KEY(brother_type_id)
        REFERENCES brother_types(id),

    CONSTRAINT fk_family_details_sister_type
        FOREIGN KEY(sister_type_id)
        REFERENCES sister_types(id)
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_family_details_profile
    ON family_details(profile_id);

CREATE INDEX idx_family_details_family
    ON family_details(family_id);

CREATE INDEX idx_family_details_deleted_at
    ON family_details(deleted_at);

CREATE INDEX idx_family_details_family_type
    ON family_details(family_type_id);

CREATE INDEX idx_family_details_brother_type
    ON family_details(brother_type_id);

CREATE INDEX idx_family_details_sister_type
    ON family_details(sister_type_id);

CREATE INDEX idx_family_details_created_at
    ON family_details(created_at);