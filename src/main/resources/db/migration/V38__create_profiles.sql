-- =====================================================
-- V38__create_profiles.sql
-- Gathbandhan Matrimony
-- Profile Master Table
-- =====================================================

CREATE TABLE profiles (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================

    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER
    -- =====================================================

    user_id BIGINT NOT NULL,

    -- =====================================================
    -- MASTER TABLE REFERENCES
    -- =====================================================

    profile_type_id BIGINT,
    manglik_status_id BIGINT,

    family_type_id BIGINT,
    family_status_id BIGINT,
    family_value_id BIGINT,

    religion_id BIGINT,
    caste_id BIGINT,
    sub_caste_id BIGINT,

    country_id BIGINT,
    state_id BIGINT,
    city_id BIGINT,

    mother_tongue_id BIGINT,

    qualification_id BIGINT,
    field_of_study_id BIGINT,

    employed_id BIGINT,
    disability_status_id BIGINT,
    blood_group_id BIGINT,

    marital_status_id BIGINT,
    gender_id BIGINT,

    education_level_id BIGINT,
    occupation_id BIGINT,

    height_id BIGINT,
    weight_id BIGINT,
    body_type_id BIGINT,
    complexion_id BIGINT,

    income_id BIGINT,

    diet_id BIGINT,
    smoking_id BIGINT,
    drinking_id BIGINT,

    -- =====================================================
    -- BASIC PROFILE
    -- =====================================================

    date_of_birth DATE,

    about VARCHAR(1000),

    about_me VARCHAR(2000),

    image_url TEXT,

    -- =====================================================
    -- CAREER
    -- =====================================================

    company_name VARCHAR(200),

    -- =====================================================
    -- ADDRESS
    -- =====================================================

    address VARCHAR(500),

    -- =====================================================
    -- FAMILY
    -- =====================================================

    father_name VARCHAR(100),

    father_occupation VARCHAR(200),

    mother_name VARCHAR(100),

    mother_occupation VARCHAR(200),

    siblings_count INTEGER,

    -- =====================================================
    -- SYSTEM
    -- =====================================================

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    current_step INTEGER DEFAULT 1,

    profile_completed BOOLEAN DEFAULT FALSE,

    is_premium BOOLEAN DEFAULT FALSE,

    premium_plan VARCHAR(30) DEFAULT 'FREE',

    premium_start_date TIMESTAMP WITHOUT TIME ZONE,

    premium_end_date TIMESTAMP WITHOUT TIME ZONE,

boost_score INTEGER DEFAULT 0,

-- =====================================================
-- AUDIT
-- =====================================================


created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

        created_by BIGINT,

        updated_at TIMESTAMP WITHOUT TIME ZONE,

        updated_by BIGINT,

        is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

        deleted_at TIMESTAMP WITHOUT TIME ZONE,

        deleted_by BIGINT,

        deletion_reason VARCHAR(500),

        version BIGINT NOT NULL DEFAULT 0
    );
   -- =====================================================
   -- UNIQUE CONSTRAINTS
   -- =====================================================

   ALTER TABLE profiles
       ADD CONSTRAINT uk_profiles_user UNIQUE (user_id);

   -- =====================================================
   -- FOREIGN KEYS
   -- =====================================================

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_user
           FOREIGN KEY (user_id) REFERENCES users(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_profile_type
           FOREIGN KEY (profile_type_id) REFERENCES profile_types(id);

ALTER TABLE profiles
    ADD CONSTRAINT fk_profiles_manglik_status
        FOREIGN KEY (manglik_status_id) REFERENCES manglik_status(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_family_type
           FOREIGN KEY (family_type_id) REFERENCES family_types(id);

  ALTER TABLE profiles
      ADD CONSTRAINT fk_profiles_family_status
          FOREIGN KEY (family_status_id) REFERENCES family_status(id);
   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_family_value
           FOREIGN KEY (family_value_id) REFERENCES family_values(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_religion
           FOREIGN KEY (religion_id) REFERENCES religions(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_caste
           FOREIGN KEY (caste_id) REFERENCES castes(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_sub_caste
           FOREIGN KEY (sub_caste_id) REFERENCES sub_castes(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_country
           FOREIGN KEY (country_id) REFERENCES countries(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_state
           FOREIGN KEY (state_id) REFERENCES states(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_city
           FOREIGN KEY (city_id) REFERENCES cities(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_mother_tongue
           FOREIGN KEY (mother_tongue_id) REFERENCES mother_tongues(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_qualification
           FOREIGN KEY (qualification_id) REFERENCES qualifications(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_field_of_study
           FOREIGN KEY (field_of_study_id) REFERENCES fields_of_study(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_employed
           FOREIGN KEY (employed_id) REFERENCES employed(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_disability_status
           FOREIGN KEY (disability_status_id) REFERENCES disability_statuses(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_blood_group
           FOREIGN KEY (blood_group_id) REFERENCES blood_groups(id);

ALTER TABLE profiles
    ADD CONSTRAINT fk_profiles_marital_status
        FOREIGN KEY (marital_status_id) REFERENCES marital_status(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_gender
           FOREIGN KEY (gender_id) REFERENCES genders(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_education_level
           FOREIGN KEY (education_level_id) REFERENCES education_levels(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_occupation
           FOREIGN KEY (occupation_id) REFERENCES occupations(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_height
           FOREIGN KEY (height_id) REFERENCES heights(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_weight
           FOREIGN KEY (weight_id) REFERENCES weights(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_body_type
           FOREIGN KEY (body_type_id) REFERENCES body_types(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_complexion
           FOREIGN KEY (complexion_id) REFERENCES complexions(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_income
           FOREIGN KEY (income_id) REFERENCES incomes(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_diet
           FOREIGN KEY (diet_id) REFERENCES diets(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_smoking
           FOREIGN KEY (smoking_id) REFERENCES smoking(id);

   ALTER TABLE profiles
       ADD CONSTRAINT fk_profiles_drinking
           FOREIGN KEY (drinking_id) REFERENCES drinking(id);

           -- =====================================================
           -- CHECK CONSTRAINTS
           -- =====================================================

           ALTER TABLE profiles
           ADD CONSTRAINT chk_profiles_premium_plan
           CHECK (
               premium_plan IN (
                   'FREE',
                   'ONE_MONTH',
                   'THREE_MONTHS',
                   'SIX_MONTHS',
                   'TWELVE_MONTHS'
               )
           );
           -- =====================================================
           -- ENTITY INDEXES
           -- =====================================================

           CREATE INDEX idx_profile_user
               ON profiles(user_id);

           CREATE INDEX idx_profile_city
               ON profiles(city_id);

           CREATE INDEX idx_profile_caste
               ON profiles(caste_id);

           CREATE INDEX idx_profile_religion
               ON profiles(religion_id);

           CREATE INDEX idx_profile_dob
               ON profiles(date_of_birth);

           CREATE INDEX idx_profile_active
               ON profiles(is_active);
               -- =====================================================
               -- PRODUCTION INDEXES
               -- =====================================================

               CREATE INDEX idx_profiles_gender
                   ON profiles(gender_id);

               CREATE INDEX idx_profiles_marital_status
                   ON profiles(marital_status_id);

               CREATE INDEX idx_profiles_education
                   ON profiles(education_level_id);

               CREATE INDEX idx_profiles_occupation
                   ON profiles(occupation_id);

               CREATE INDEX idx_profiles_income
                   ON profiles(income_id);

               CREATE INDEX idx_profiles_height
                   ON profiles(height_id);

               CREATE INDEX idx_profiles_weight
                   ON profiles(weight_id);

               CREATE INDEX idx_profiles_body_type
                   ON profiles(body_type_id);

               CREATE INDEX idx_profiles_complexion
                   ON profiles(complexion_id);

               CREATE INDEX idx_profiles_country
                   ON profiles(country_id);

               CREATE INDEX idx_profiles_state
                   ON profiles(state_id);

               CREATE INDEX idx_profiles_mother_tongue
                   ON profiles(mother_tongue_id);

               CREATE INDEX idx_profiles_profile_type
                   ON profiles(profile_type_id);

               CREATE INDEX idx_profiles_premium
                   ON profiles(is_premium);

               CREATE INDEX idx_profiles_completed
                   ON profiles(profile_completed);

               CREATE INDEX idx_profiles_deleted
                   ON profiles(is_deleted);

               CREATE INDEX idx_profiles_created_at
                   ON profiles(created_at);