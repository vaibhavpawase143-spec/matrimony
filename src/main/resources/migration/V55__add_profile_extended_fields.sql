-- Add extended profile fields for complete profile management
-- Migration V55

-- Add sub caste foreign key
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS sub_caste_id BIGINT;
ALTER TABLE profiles ADD CONSTRAINT fk_profile_sub_caste FOREIGN KEY (sub_caste_id) REFERENCES sub_castes(id) ON DELETE SET NULL;

-- Add physical details
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS complexion VARCHAR(50);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS body_type VARCHAR(50);

-- Add education & career details
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS annual_income VARCHAR(100);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS company_name VARCHAR(200);

-- Add location details
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS country VARCHAR(100);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS state VARCHAR(100);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS address VARCHAR(500);

-- Add lifestyle details
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS diet VARCHAR(50);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS smoking VARCHAR(50);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS drinking VARCHAR(50);

-- Add family details
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS father_name VARCHAR(100);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS father_occupation VARCHAR(200);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS mother_name VARCHAR(100);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS mother_occupation VARCHAR(200);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS siblings_count VARCHAR(50);

-- Add partner preferences
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS preferred_age_min VARCHAR(10);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS preferred_age_max VARCHAR(10);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS preferred_location VARCHAR(200);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS preferred_education VARCHAR(200);
ALTER TABLE profiles ADD COLUMN IF NOT EXISTS other_expectations VARCHAR(1000);

-- Create indexes for frequently queried fields
CREATE INDEX IF NOT EXISTS idx_profile_country ON profiles(country);
CREATE INDEX IF NOT EXISTS idx_profile_state ON profiles(state);
CREATE INDEX IF NOT EXISTS idx_profile_sub_caste ON profiles(sub_caste_id);
