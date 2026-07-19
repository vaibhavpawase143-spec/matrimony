-- =====================================================
-- GATHBANDHAN MATRIMONY
-- COUNTRY MASTER DATA
-- =====================================================

-- -----------------------------------------------------
-- DEFAULT COUNTRY
-- -----------------------------------------------------

INSERT INTO countries (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    a.id,
    c.country_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('India')
) AS c(country_name)
CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a
WHERE NOT EXISTS (
    SELECT 1
    FROM countries ct
    WHERE LOWER(ct.name) = LOWER(c.country_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- Uncomment while testing if needed.
-- -----------------------------------------------------
-- SELECT * FROM countries;