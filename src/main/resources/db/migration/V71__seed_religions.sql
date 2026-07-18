-- =====================================================
-- GATHBANDHAN MATRIMONY
-- RELIGION MASTER DATA
-- =====================================================

-- -----------------------------------------------------
-- DEFAULT RELIGIONS
-- -----------------------------------------------------

INSERT INTO religions (
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
    r.religion_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Hindu'),
        ('Muslim'),
        ('Christian'),
        ('Sikh'),
        ('Jain'),
        ('Buddhist'),
        ('Parsi'),
        ('Jewish'),
        ('Bahá''í'),
        ('Tribal'),
        ('Other')
) AS r(religion_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM religions rel
    WHERE rel.admin_id = a.id
      AND LOWER(rel.name) = LOWER(r.religion_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT * FROM religions ORDER BY name;