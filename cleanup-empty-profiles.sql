-- SQL Script to identify and delete broken/empty profile records
-- Run this script to clean up the database

-- First, identify empty/broken profiles
SELECT 
    p.id as profile_id,
    p.user_id,
    u.email,
    p.created_at,
    CASE 
        WHEN (p.first_name IS NULL OR p.first_name = '') AND 
             (p.last_name IS NULL OR p.last_name = '') AND
             (p.gender IS NULL OR p.gender = '') AND
             (p.date_of_birth IS NULL) AND
             (p.religion_id IS NULL) AND
             (p.city_id IS NULL) AND
             (p.education_level_id IS NULL) AND
             (p.occupation_id IS NULL)
        THEN 'EMPTY'
        WHEN (p.first_name IS NULL OR p.first_name = '') OR 
             (p.last_name IS NULL OR p.last_name = '') OR
             (p.gender IS NULL OR p.gender = '') OR
             (p.date_of_birth IS NULL) OR
             (p.religion_id IS NULL)
        THEN 'INCOMPLETE'
        ELSE 'VALID'
    END as profile_status
FROM profiles p
JOIN users u ON p.user_id = u.id
WHERE p.is_active = true
ORDER BY p.created_at DESC;

-- Delete completely empty profiles (no essential data)
DELETE FROM profiles 
WHERE id IN (
    SELECT p.id 
    FROM profiles p
    WHERE 
        (p.first_name IS NULL OR p.first_name = '') AND 
        (p.last_name IS NULL OR p.last_name = '') AND
        (p.gender IS NULL OR p.gender = '') AND
        (p.date_of_birth IS NULL) AND
        (p.religion_id IS NULL) AND
        (p.city_id IS NULL) AND
        (p.education_level_id IS NULL) AND
        (p.occupation_id IS NULL)
);

-- Alternative: Delete profiles older than 30 days with no essential data
DELETE FROM profiles 
WHERE id IN (
    SELECT p.id 
    FROM profiles p
    WHERE 
        p.created_at < DATE_SUB(NOW(), INTERVAL 30 DAY) AND
        (
            (p.first_name IS NULL OR p.first_name = '') OR 
            (p.last_name IS NULL OR p.last_name = '') OR
            (p.gender IS NULL OR p.gender = '') OR
            (p.date_of_birth IS NULL) OR
            (p.religion_id IS NULL) OR
            (p.city_id IS NULL)
        )
);

-- Update statistics after cleanup
SELECT 
    COUNT(*) as total_profiles,
    COUNT(CASE WHEN p.is_active = true THEN 1 END) as active_profiles,
    COUNT(CASE WHEN p.profile_completed = true THEN 1 END) as completed_profiles,
    COUNT(CASE WHEN p.profile_completed = false THEN 1 END) as incomplete_profiles
FROM profiles p;

-- Verify cleanup was successful
SELECT 'Cleanup completed successfully' as status;
