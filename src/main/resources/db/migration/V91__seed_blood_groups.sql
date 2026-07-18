-- =====================================================
-- GATHBANDHAN MATRIMONY
-- BLOOD GROUP MASTER DATA
-- =====================================================

INSERT INTO blood_groups (
    admin_id,
    type,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    bg.blood_type,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('A+'),
        ('A-'),
        ('B+'),
        ('B-'),
        ('AB+'),
        ('AB-'),
        ('O+'),
        ('O-')
) AS bg(blood_type)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM blood_groups b
    WHERE UPPER(b.type) = UPPER(bg.blood_type)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM blood_groups;
-- SELECT * FROM blood_groups ORDER BY type;