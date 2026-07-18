-- =====================================================
-- GATHBANDHAN MATRIMONY
-- BROTHER TYPE MASTER DATA
-- =====================================================

INSERT INTO brother_types (
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
    bt.type_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Married'),
        ('Unmarried'),
        ('Married & Unmarried'),
        ('No Brothers'),
        ('Prefer Not to Say')
) AS bt(type_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM brother_types b
    WHERE b.admin_id = a.id
      AND LOWER(b.value) = LOWER(bt.type_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM brother_types;
-- SELECT * FROM brother_types ORDER BY id;