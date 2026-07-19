-- =====================================================
-- GATHBANDHAN MATRIMONY
-- FAMILY TYPE MASTER DATA
-- =====================================================

INSERT INTO family_types (
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
    ft.type_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Joint Family'),
        ('Nuclear Family'),
        ('Extended Family'),
        ('Single Parent Family'),
        ('Other')
) AS ft(type_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM family_types f
    WHERE f.admin_id = a.id
      AND LOWER(f.name) = LOWER(ft.type_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM family_types;
-- SELECT * FROM family_types ORDER BY id;