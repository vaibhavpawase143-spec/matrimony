-- Insert default cities for India states (example)

INSERT INTO cities (
    name,
    state_id,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
('Mumbai', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Pune', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ahmedabad', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Surat', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Chennai', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Bangalore', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Hyderabad', (SELECT id FROM states WHERE name='Telangana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kolkata', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Jaipur', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Other', (SELECT id FROM states WHERE name='Other'), NULL, TRUE, CURRENT_TIMESTAMP, NULL);