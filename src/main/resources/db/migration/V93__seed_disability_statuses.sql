-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DISABILITY STATUS MASTER DATA
-- =====================================================

INSERT INTO disability_statuses (
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
    ds.status_value,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('No Disability'),
        ('Physically Challenged'),
        ('Visually Challenged'),
        ('Hearing Impaired'),
        ('Speech Impaired'),
        ('Other'),
        ('Prefer Not to Say')
) AS ds(status_value)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM disability_statuses d
    WHERE d.admin_id = a.id
      AND LOWER(d.value) = LOWER(ds.status_value)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM disability_statuses;
-- SELECT * FROM disability_statuses ORDER BY id;