-- =====================================================
-- GATHBANDHAN MATRIMONY
-- FAMILY VALUE MASTER DATA
-- =====================================================

INSERT INTO family_values (
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
    fv.value_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Traditional'),
        ('Moderate'),
        ('Liberal'),
        ('Orthodox'),
        ('Progressive'),
        ('Other')
) AS fv(value_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM family_values f
    WHERE f.admin_id = a.id
      AND LOWER(f.name) = LOWER(fv.value_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM family_values;
-- SELECT * FROM family_values ORDER BY id;