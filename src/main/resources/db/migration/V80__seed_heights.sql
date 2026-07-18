-- =====================================================
-- GATHBANDHAN MATRIMONY
-- HEIGHT MASTER DATA
-- =====================================================

INSERT INTO heights (
    admin_id,
    height,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    h.height_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('4''6" (137 cm)'),
        ('4''7" (140 cm)'),
        ('4''8" (142 cm)'),
        ('4''9" (145 cm)'),
        ('4''10" (147 cm)'),
        ('4''11" (150 cm)'),
        ('5''0" (152 cm)'),
        ('5''1" (155 cm)'),
        ('5''2" (157 cm)'),
        ('5''3" (160 cm)'),
        ('5''4" (163 cm)'),
        ('5''5" (165 cm)'),
        ('5''6" (168 cm)'),
        ('5''7" (170 cm)'),
        ('5''8" (173 cm)'),
        ('5''9" (175 cm)'),
        ('5''10" (178 cm)'),
        ('5''11" (180 cm)'),
        ('6''0" (183 cm)'),
        ('6''1" (185 cm)'),
        ('6''2" (188 cm)'),
        ('6''3" (191 cm)'),
        ('6''4" (193 cm)'),
        ('6''5" (196 cm)'),
        ('6''6" (198 cm)'),
        ('6''7" (201 cm)'),
        ('6''8" (203 cm)'),
        ('6''9" (206 cm)'),
        ('6''10" (208 cm)'),
        ('6''11" (211 cm)'),
        ('7''0" (213 cm)')
) AS h(height_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM heights ht
    WHERE ht.admin_id = a.id
      AND LOWER(ht.height) = LOWER(h.height_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM heights;
-- SELECT * FROM heights ORDER BY id;