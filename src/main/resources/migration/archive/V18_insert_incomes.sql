-- Insert default income ranges (annual)

INSERT INTO incomes (
    admin_id,
    range,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Below 1 Lakh', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '1 - 3 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '3 - 5 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5 - 10 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '10 - 20 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '20 - 50 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Above 50 Lakhs', TRUE, CURRENT_TIMESTAMP, NULL);