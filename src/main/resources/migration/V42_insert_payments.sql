-- Insert demo payments for users

INSERT INTO payments (
    user_id,
    amount,
    payment_method,
    transaction_id,
    status,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    499.99,
    'Credit Card',
    'TXN-JDOE-001',
    'SUCCESS',
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    1499.99,
    'UPI',
    'TXN-JSMITH-001',
    'SUCCESS',
    CURRENT_TIMESTAMP,
    NULL
);