-- =====================================================
-- V101__seed_subscription_plans.sql
-- Subscription Plans Master Data
-- =====================================================

INSERT INTO subscription_plans (
    admin_id,
    name,
    price,
    duration,
    description,
    is_active,
    created_at,
    updated_at
)
SELECT
    a.id,
    p.name,
    p.price,
    p.duration,
    p.description,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        (
            'Free',
            0.00,
            0,
            'Basic membership with limited features.'
        ),
        (
            'Premium 1 Month',
            499.00,
            30,
            'Premium membership valid for 30 days.'
        ),
        (
            'Premium 3 Months',
            1199.00,
            90,
            'Premium membership valid for 90 days.'
        ),
        (
            'Premium 6 Months',
            1999.00,
            180,
            'Premium membership valid for 180 days.'
        ),
        (
            'Premium 12 Months',
            3499.00,
            365,
            'Premium membership valid for one year.'
        ),
        (
            'Premium 24 Months',
            5999.00,
            730,
            'Premium membership valid for two years.'
        )
) AS p (
    name,
    price,
    duration,
    description
)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM subscription_plans sp
    WHERE sp.admin_id = a.id
      AND LOWER(sp.name) = LOWER(p.name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT * FROM subscription_plans ORDER BY id;