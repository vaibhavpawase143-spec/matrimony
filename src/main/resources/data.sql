-- Insert master data for matrimony application
-- This file will be executed by Spring Boot SQL initialization

-- Religions data
INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Hindu', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Hindu');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Muslim', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Muslim');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Christian', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Christian');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Sikh', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Sikh');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Buddhist', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Buddhist');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Jain', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Jain');

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM religions WHERE name = 'Other');

-- Marital Statuses
INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Single', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Single');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Married', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Married');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Divorced', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Divorced');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Widowed', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Widowed');

-- Education Levels
INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, '10th', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = '10th');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, '12th', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = '12th');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bachelor''s Degree', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Bachelor''s Degree');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Master''s Degree', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Master''s Degree');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'PhD', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'PhD');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Professional Degree', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Professional Degree');

-- Occupations
INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Software Engineer', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Software Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Doctor', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Doctor');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Teacher', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Teacher');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Engineer', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Business', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Business');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Government Employee', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Government Employee');

-- Cities (major Indian cities)
INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Mumbai', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Mumbai');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Delhi', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Delhi');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Bangalore', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Bangalore');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Chennai', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Chennai');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Kolkata', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Kolkata');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Hyderabad', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Hyderabad');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Pune', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Pune');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Ahmedabad', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Ahmedabad');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Jaipur', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Jaipur');

INSERT INTO cities (name, state_id, admin_id, is_active, created_at, updated_at)
SELECT 'Other', NULL, NULL, TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM cities WHERE name = 'Other');

-- Mother Tongues
INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Hindi', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Hindi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'English', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'English');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Marathi', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Marathi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Gujarati', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Gujarati');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bengali', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bengali');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Tamil', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Tamil');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Telugu', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Telugu');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Kannada', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Kannada');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Punjabi', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Punjabi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Other');
