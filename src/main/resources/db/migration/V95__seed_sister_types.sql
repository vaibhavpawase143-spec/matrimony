-- =====================================================
-- GATHBANDHAN MATRIMONY
-- SISTER TYPE MASTER DATA
-- =====================================================

INSERT INTO sister_types (
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
    st.type_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('Married'),
        ('Unmarried'),
        ('Married & Unmarried'),
        ('No Sisters'),
        ('Prefer Not to Say')
) AS st(type_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM sister_types s
    WHERE s.admin_id = a.id
      AND LOWER(s.value) = LOWER(st.type_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM sister_types;
-- SELECT * FROM sister_types ORDER BY id;