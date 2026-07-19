-- =====================================================
-- GATHBANDHAN MATRIMONY
-- GENDER MASTER DATA
-- =====================================================

INSERT INTO genders (
    name,
    is_active,
    created_at,
    updated_at
)
SELECT
    g.gender_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('Male'),
        ('Female'),
        ('Other')
) AS g(gender_name)

WHERE NOT EXISTS (
    SELECT 1
    FROM genders gd
    WHERE LOWER(gd.name) = LOWER(g.gender_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM genders;
-- SELECT * FROM genders ORDER BY id;