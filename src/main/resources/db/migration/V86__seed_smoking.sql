-- =====================================================
-- GATHBANDHAN MATRIMONY
-- SMOKING MASTER DATA
-- =====================================================

INSERT INTO smoking (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    s.smoking_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Non Smoker'),
        ('Occasionally'),
        ('Social Smoker'),
        ('Regular Smoker'),
        ('Trying to Quit'),
        ('Prefer Not to Say')
) AS s(smoking_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM smoking sm
    WHERE sm.admin_id = a.id
      AND LOWER(sm.value) = LOWER(s.smoking_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM smoking;
-- SELECT * FROM smoking ORDER BY id;