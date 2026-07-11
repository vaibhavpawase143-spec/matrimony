-- V39__create_family_details.sql

CREATE TABLE family_details (
    id BIGSERIAL PRIMARY KEY,

    profile_id BIGINT UNIQUE,

    family_type_id BIGINT,
    family_id BIGINT,
    brother_type_id BIGINT,
    sister_type_id BIGINT,

    father_occupation VARCHAR(150),
    mother_occupation VARCHAR(150),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_family_details_profile
        FOREIGN KEY (profile_id)
        REFERENCES profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_family_details_family_type
        FOREIGN KEY (family_type_id)
        REFERENCES family_types(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_family_details_family
        FOREIGN KEY (family_id)
        REFERENCES family(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_family_details_brother_type
        FOREIGN KEY (brother_type_id)
        REFERENCES brother_types(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_family_details_sister_type
        FOREIGN KEY (sister_type_id)
        REFERENCES sister_types(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_family_details_profile 
ON family_details(profile_id);

CREATE INDEX idx_family_details_family 
ON family_details(family_id);

CREATE INDEX idx_family_details_family_type 
ON family_details(family_type_id);

CREATE INDEX idx_family_details_brother_type 
ON family_details(brother_type_id);

CREATE INDEX idx_family_details_sister_type 
ON family_details(sister_type_id);