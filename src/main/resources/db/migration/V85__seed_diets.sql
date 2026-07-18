-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DIET MASTER DATA
-- =====================================================

INSERT INTO diets (
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
    d.diet_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Vegetarian'),
        ('Eggetarian'),
        ('Vegan'),
        ('Jain'),
        ('Non Vegetarian'),
        ('Occasionally Non Vegetarian'),
        ('Halal'),
        ('Other')
) AS d(diet_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM diets dt
    WHERE dt.admin_id = a.id
      AND LOWER(dt.name) = LOWER(d.diet_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM diets;
-- SELECT * FROM diets ORDER BY id;