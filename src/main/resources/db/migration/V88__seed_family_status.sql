-- =====================================================
-- GATHBANDHAN MATRIMONY
-- FAMILY STATUS MASTER DATA
-- =====================================================

INSERT INTO family_status (
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
    fs.status_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Middle Class'),
        ('Upper Middle Class'),
        ('Rich / Affluent'),
        ('High Net Worth'),
        ('Other')
) AS fs(status_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM family_status f
    WHERE f.admin_id = a.id
      AND LOWER(f.name) = LOWER(fs.status_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM family_status;
-- SELECT * FROM family_status ORDER BY id;