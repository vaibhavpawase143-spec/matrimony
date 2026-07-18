-- =====================================================
-- GATHBANDHAN MATRIMONY
-- COMPLEXION MASTER DATA
-- =====================================================

INSERT INTO complexions (
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
    c.complexion_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Very Fair'),
        ('Fair'),
        ('Wheatish'),
        ('Wheatish Brown'),
        ('Brown'),
        ('Dark'),
        ('Prefer Not to Say'),
        ('Other')
) AS c(complexion_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM complexions cmp
    WHERE cmp.admin_id = a.id
      AND LOWER(cmp.value) = LOWER(c.complexion_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM complexions;
-- SELECT * FROM complexions ORDER BY id;