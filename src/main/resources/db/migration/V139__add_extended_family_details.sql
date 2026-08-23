-- =====================================================
-- V139: Add Extended Family Details
-- =====================================================

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS aunt VARCHAR(200);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS sisters_count INTEGER;

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS brothers_count INTEGER;

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS nanihal_details VARCHAR(1000);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS best_friend VARCHAR(200);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS uncles_count INTEGER;

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS uncle_1_name VARCHAR(200);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS uncle_2_name VARCHAR(200);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS uncle_3_name VARCHAR(200);

ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS uncle_4_name VARCHAR(200);