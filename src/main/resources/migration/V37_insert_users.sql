-- Insert default demo users

INSERT INTO users (
    first_name,
    last_name,
    email,
    phone,
    password,
    is_active,
    email_verified_at,
    phone_verified_at,
    created_at,
    updated_at
) VALUES
('John', 'Doe', 'john.doe@example.com', '9999000001', '$2a$12$demohashedpassword1', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Jane', 'Smith', 'jane.smith@example.com', '9999000002', '$2a$12$demohashedpassword2', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Alice', 'Johnson', 'alice.johnson@example.com', '9999000003', '$2a$12$demohashedpassword3', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('Bob', 'Williams', 'bob.williams@example.com', '9999000004', '$2a$12$demohashedpassword4', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);