ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS education_other VARCHAR(255);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS qualification_other VARCHAR(255);