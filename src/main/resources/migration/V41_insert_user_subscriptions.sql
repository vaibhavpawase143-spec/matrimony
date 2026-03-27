-- Insert demo subscriptions for users

INSERT INTO user_subscriptions (
    user_id,
    plan_id,
    start_date,
    end_date,
    is_active,
    status,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    (SELECT id FROM subscription_plans WHERE name='Basic Plan'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '30 days',
    TRUE,
    'ACTIVE',
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    (SELECT id FROM subscription_plans WHERE name='Premium Plan'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '90 days',
    TRUE,
    'ACTIVE',
    CURRENT_TIMESTAMP,
    NULL
);