-- =====================================================
-- GATHBANDHAN MATRIMONY
-- EMPLOYMENT STATUS MASTER DATA
-- =====================================================

INSERT INTO employed (
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
    e.status_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Employed'),
        ('Self Employed'),
        ('Business Owner'),
        ('Government Employee'),
        ('Not Employed'),
        ('Student'),
        ('Retired'),
        ('Other')
) AS e(status_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM employed emp
    WHERE emp.admin_id = a.id
      AND LOWER(emp.name) = LOWER(e.status_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM employed;
-- SELECT * FROM employed ORDER BY id;