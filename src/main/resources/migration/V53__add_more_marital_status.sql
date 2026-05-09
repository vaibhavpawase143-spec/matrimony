-- Add more comprehensive marital status data

INSERT INTO marital_status (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Never Married', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Unmarried', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Single', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Awaiting Divorce', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Divorced', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Divorcee', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Widowed', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Widow', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Widower', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Separated', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Annulled', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Prefer Not to Say', TRUE, CURRENT_TIMESTAMP, NULL)
ON CONFLICT (name) DO NOTHING;
