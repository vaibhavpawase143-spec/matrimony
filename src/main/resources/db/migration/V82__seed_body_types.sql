-- =====================================================
-- GATHBANDHAN MATRIMONY
-- BODY TYPE MASTER DATA
-- =====================================================

INSERT INTO body_types (
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
    bt.body_type,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Slim'),
        ('Average'),
        ('Athletic'),
        ('Fit'),
        ('Muscular'),
        ('Heavy'),
        ('A Few Extra Pounds'),
        ('Other')
) AS bt(body_type)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM body_types b
    WHERE b.admin_id = a.id
      AND LOWER(b.value) = LOWER(bt.body_type)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM body_types;
-- SELECT * FROM body_types ORDER BY id;