-- =====================================================
-- V124__cleanup_duplicate_sub_castes.sql
-- Safely clean duplicate sub-caste records and preserve references
-- =====================================================

-- Step 1: Re-map profiles referencing duplicate sub_castes to the MIN(id) keeper record
UPDATE profiles p
SET sub_caste_id = keeper.min_id
FROM (
    SELECT caste_id, LOWER(TRIM(name)) AS lower_name, MIN(id) AS min_id
    FROM sub_castes
    WHERE deleted_at IS NULL
    GROUP BY caste_id, LOWER(TRIM(name))
    HAVING COUNT(*) > 1
) keeper
JOIN sub_castes dup
  ON dup.caste_id = keeper.caste_id
 AND LOWER(TRIM(dup.name)) = keeper.lower_name
 AND dup.id != keeper.min_id
WHERE p.sub_caste_id = dup.id;

-- Step 2: Delete duplicate active sub_castes keeping only MIN(id)
DELETE FROM sub_castes
WHERE id IN (
    SELECT dup.id
    FROM (
        SELECT caste_id, LOWER(TRIM(name)) AS lower_name, MIN(id) AS min_id
        FROM sub_castes
        WHERE deleted_at IS NULL
        GROUP BY caste_id, LOWER(TRIM(name))
        HAVING COUNT(*) > 1
    ) keeper
    JOIN sub_castes dup
      ON dup.caste_id = keeper.caste_id
     AND LOWER(TRIM(dup.name)) = keeper.lower_name
     AND dup.id != keeper.min_id
);
