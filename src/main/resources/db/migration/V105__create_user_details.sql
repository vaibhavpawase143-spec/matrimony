-- =====================================================
-- V105__create_user_details.sql
-- User Details Table
-- =====================================================

CREATE TABLE user_details
(
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT,

    full_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    is_active BOOLEAN,

    religion VARCHAR(100),
    caste VARCHAR(100),
    education VARCHAR(255),
    occupation VARCHAR(255),
    height VARCHAR(50),
    weight VARCHAR(50),
    city VARCHAR(150),
    state VARCHAR(150),
    country VARCHAR(150),

    about VARCHAR(1000),

    family_type VARCHAR(100),
    family VARCHAR(100),
    brother_type VARCHAR(100),
    sister_type VARCHAR(100),
    father_occupation VARCHAR(255),
    mother_occupation VARCHAR(255),

    min_age INTEGER,
    max_age INTEGER,

    min_height BIGINT,
    max_height BIGINT,

    preferred_religion VARCHAR(100),
    preferred_caste VARCHAR(100),
    preferred_city VARCHAR(150),

    plan_name VARCHAR(255),
    subscription_status VARCHAR(100),
    payment_status VARCHAR(100),

    password VARCHAR(255),

    created_at TIMESTAMP
);

-- =====================================================
-- USER ROLES
-- =====================================================

CREATE TABLE user_details_roles
(
    user_details_id BIGINT NOT NULL,
    role VARCHAR(100) NOT NULL,

    CONSTRAINT fk_user_details_roles
        FOREIGN KEY (user_details_id)
        REFERENCES user_details(id)
        ON DELETE CASCADE
);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_user_details_user
    ON user_details(user_id);

CREATE INDEX idx_user_details_email
    ON user_details(email);

CREATE INDEX idx_user_details_phone
    ON user_details(phone);

CREATE INDEX idx_user_details_roles
    ON user_details_roles(user_details_id);