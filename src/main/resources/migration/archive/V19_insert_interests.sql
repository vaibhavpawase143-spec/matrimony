-- Insert sample interests (user interactions)

INSERT INTO interests (
    sender_id,
    receiver_id,
    status,
    is_active,
    created_at,
    updated_at
) VALUES
(1, 2, 'PENDING', TRUE, CURRENT_TIMESTAMP, NULL),
(2, 3, 'ACCEPTED', TRUE, CURRENT_TIMESTAMP, NULL),
(3, 1, 'REJECTED', TRUE, CURRENT_TIMESTAMP, NULL);