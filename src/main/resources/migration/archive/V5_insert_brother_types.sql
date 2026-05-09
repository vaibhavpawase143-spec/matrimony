-- Insert default brother types

INSERT INTO brother_types (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'No Brother', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '1 Brother', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '2 Brothers', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '3+ Brothers', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '1 Married Brother', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '2 Married Brothers', TRUE, CURRENT_TIMESTAMP, NULL);