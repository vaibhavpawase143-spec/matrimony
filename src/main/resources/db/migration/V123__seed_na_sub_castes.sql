-- =====================================================
-- V123__seed_na_sub_castes.sql
-- Seed "N/A" Sub-Caste for Castes without Sub-Castes
-- =====================================================

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    COALESCE(a.id, 1),
    c.id,
    'N/A',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM castes c
LEFT JOIN (
    SELECT id FROM admins WHERE username = 'superadmin' LIMIT 1
) a ON 1=1
WHERE NOT EXISTS (
    SELECT 1 FROM sub_castes s WHERE s.caste_id = c.id
);
