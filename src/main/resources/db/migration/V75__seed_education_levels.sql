-- =====================================================
-- GATHBANDHAN MATRIMONY
-- EDUCATION LEVEL MASTER DATA
-- =====================================================

INSERT INTO education_levels (
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
    e.level_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('No Formal Education'),
        ('Primary School'),
        ('Middle School'),
        ('Secondary School'),
        ('Higher Secondary'),
        ('ITI'),
        ('Diploma'),
        ('Undergraduate'),
        ('Graduate'),
        ('Postgraduate'),
        ('Doctorate'),
        ('Post Doctorate'),
        ('Professional Certification'),
        ('Vocational Training'),
        ('Other')
) AS e(level_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM education_levels el
    WHERE el.admin_id = a.id
      AND LOWER(el.name) = LOWER(e.level_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM education_levels;
-- SELECT * FROM education_levels ORDER BY name;