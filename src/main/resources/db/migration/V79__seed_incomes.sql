-- =====================================================
-- GATHBANDHAN MATRIMONY
-- INCOME MASTER DATA
-- =====================================================

INSERT INTO incomes (
    admin_id,
    range,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    i.income_range,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES
        ('No Income'),
        ('Below ₹1 Lakh'),
        ('₹1 Lakh - ₹2 Lakhs'),
        ('₹2 Lakhs - ₹3 Lakhs'),
        ('₹3 Lakhs - ₹5 Lakhs'),
        ('₹5 Lakhs - ₹7 Lakhs'),
        ('₹7 Lakhs - ₹10 Lakhs'),
        ('₹10 Lakhs - ₹15 Lakhs'),
        ('₹15 Lakhs - ₹20 Lakhs'),
        ('₹20 Lakhs - ₹30 Lakhs'),
        ('₹30 Lakhs - ₹50 Lakhs'),
        ('₹50 Lakhs - ₹75 Lakhs'),
        ('₹75 Lakhs - ₹1 Crore'),
        ('₹1 Crore - ₹2 Crore'),
        ('₹2 Crore - ₹5 Crore'),
        ('Above ₹5 Crore'),
        ('Prefer Not to Say')
) AS i(income_range)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM incomes inc
    WHERE inc.admin_id = a.id
      AND LOWER(inc.range) = LOWER(i.income_range)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM incomes;
-- SELECT * FROM incomes ORDER BY id;