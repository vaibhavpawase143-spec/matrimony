-- =====================================================
-- GATHBANDHAN MATRIMONY
-- MARITAL STATUS MASTER DATA
-- =====================================================

INSERT INTO marital_status (
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
    ms.status_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Never Married'),
        ('Divorced'),
        ('Awaiting Divorce'),
        ('Separated'),
        ('Widowed'),
        ('Annulled')
) AS ms(status_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM marital_status m
    WHERE m.admin_id = a.id
      AND LOWER(m.name) = LOWER(ms.status_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM marital_status;
-- SELECT * FROM marital_status ORDER BY id;