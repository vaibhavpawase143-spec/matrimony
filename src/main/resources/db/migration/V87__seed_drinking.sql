-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DRINKING MASTER DATA
-- =====================================================

INSERT INTO drinking (
    name,
    value,
    admin_id,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    d.display_name,
    d.value_name,
    a.id,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Non Drinker', 'Non Drinker'),
        ('Occasionally', 'Occasionally'),
        ('Socially', 'Socially'),
        ('Regularly', 'Regularly'),
        ('Trying to Quit', 'Trying to Quit'),
        ('Prefer Not to Say', 'Prefer Not to Say')
) AS d(display_name, value_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM drinking dr
    WHERE dr.admin_id = a.id
      AND LOWER(dr.value) = LOWER(d.value_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM drinking;
-- SELECT * FROM drinking ORDER BY id;