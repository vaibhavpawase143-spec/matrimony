-- =====================================================
-- GATHBANDHAN MATRIMONY
-- PROFILE TYPE MASTER DATA
-- =====================================================

INSERT INTO profile_types (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    pt.profile_type,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Self'),
        ('Son'),
        ('Daughter'),
        ('Brother'),
        ('Sister'),
        ('Relative'),
        ('Friend'),
        ('Other')
) AS pt(profile_type)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM profile_types p
    WHERE p.admin_id = a.id
      AND LOWER(p.name) = LOWER(pt.profile_type)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM profile_types;
-- SELECT * FROM profile_types ORDER BY id;