CREATE TABLE user_details (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    full_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    religion VARCHAR(100),
    caste VARCHAR(100),
    education VARCHAR(100),
    occupation VARCHAR(100),
    height VARCHAR(50),
    weight VARCHAR(50),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),

    about TEXT,

    family_type VARCHAR(100),
    family VARCHAR(100),
    brother_type VARCHAR(100),
    sister_type VARCHAR(100),
    father_occupation VARCHAR(150),
    mother_occupation VARCHAR(150),

    min_age INTEGER,
    max_age INTEGER,
    min_height DOUBLE PRECISION,
    max_height DOUBLE PRECISION,

    preferred_religion VARCHAR(100),
    preferred_caste VARCHAR(100),
    preferred_city VARCHAR(100),

    plan_name VARCHAR(100),
    subscription_status VARCHAR(50),

    payment_status VARCHAR(50),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- 🔥 FK
    CONSTRAINT fk_user_details_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

-- 🔥 Indexes
CREATE INDEX idx_user_details_user ON user_details(user_id);
CREATE INDEX idx_user_details_city ON user_details(city);
CREATE INDEX idx_user_details_caste ON user_details(caste);
CREATE INDEX idx_user_details_religion ON user_details(religion);