-- =====================================================
-- GATHBANDHAN MATRIMONY
-- WEIGHT MASTER DATA
-- =====================================================

INSERT INTO weights (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    w.weight_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        ('30 kg'),
        ('35 kg'),
        ('40 kg'),
        ('45 kg'),
        ('50 kg'),
        ('55 kg'),
        ('60 kg'),
        ('65 kg'),
        ('70 kg'),
        ('75 kg'),
        ('80 kg'),
        ('85 kg'),
        ('90 kg'),
        ('95 kg'),
        ('100 kg'),
        ('105 kg'),
        ('110 kg'),
        ('115 kg'),
        ('120 kg'),
        ('125 kg'),
        ('130 kg'),
        ('135 kg'),
        ('140 kg'),
        ('145 kg'),
        ('150 kg')
) AS w(weight_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM weights wt
    WHERE wt.admin_id = a.id
      AND LOWER(wt.value) = LOWER(w.weight_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM weights;
-- SELECT * FROM weights ORDER BY id;