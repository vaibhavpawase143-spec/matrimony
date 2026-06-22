-- Insert master data for matrimony application
-- This file will be executed by Spring Boot SQL initialization

-- Religions data
INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Hindu',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Hindu'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Muslim',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Muslim'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Christian',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Christian'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Sikh',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Sikh'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Buddhist',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Buddhist'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Jain',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Jain'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Parsi',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Parsi'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Jewish',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Jewish'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Tribal',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Tribal'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Spiritual',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Spiritual'
);

INSERT INTO religions
(admin_id, name, status, is_active, created_at, updated_at)

SELECT
    1,
    'Other',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM religions WHERE name = 'Other'
);

-- Marital Statuses
INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Single', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Single');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Unmarried', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Unmarried');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Never Married', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Never Married');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Married', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Married');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Divorced', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Divorced');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Widowed', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Widowed');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Separated', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Separated');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Awaiting Divorce', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Awaiting Divorce');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Annulled', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Annulled');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Complicated', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Complicated');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Remarriage', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Remarriage');

INSERT INTO marital_status (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Partner Deceased', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM marital_status WHERE name = 'Partner Deceased');

-- Education Levels
INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, '10th', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = '10th');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, '12th', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = '12th');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Diploma', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Diploma');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'ITI', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'ITI');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.A', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.A');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.Com', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.Com');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.Sc', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.Sc');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'BCA', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'BCA');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'BBA', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'BBA');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.E', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.E');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.Tech', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.Tech');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'LLB', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'LLB');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'MBBS', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'MBBS');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'B.Pharm', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'B.Pharm');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'MBA', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'MBA');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'MCA', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'MCA');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'M.Tech', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'M.Tech');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'M.Sc', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'M.Sc');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'M.Com', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'M.Com');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'CA', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'CA');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'CS', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'CS');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'PhD', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'PhD');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Doctorate', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Doctorate');

INSERT INTO education_levels (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM education_levels WHERE name = 'Other');
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
SELECT NULL, 'Professor', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Professor');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Lecturer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Lecturer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Civil Engineer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Civil Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Mechanical Engineer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Mechanical Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Electrical Engineer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Electrical Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Electronics Engineer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Electronics Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Architect', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Architect');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Lawyer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Lawyer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Advocate', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Advocate');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Chartered Accountant', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Chartered Accountant');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Company Secretary', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Company Secretary');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bank Employee', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Bank Employee');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Government Employee', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Government Employee');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Police Officer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Police Officer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Army Officer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Army Officer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Navy Officer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Navy Officer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Air Force Officer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Air Force Officer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Farmer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Farmer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Business Owner', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Business Owner');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Entrepreneur', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Entrepreneur');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Shop Owner', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Shop Owner');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Accountant', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Accountant');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Data Analyst', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Data Analyst');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Data Scientist', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Data Scientist');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'DevOps Engineer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'DevOps Engineer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'UI UX Designer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'UI UX Designer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Graphic Designer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Graphic Designer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Interior Designer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Interior Designer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Fashion Designer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Fashion Designer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Pilot', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Pilot');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Cabin Crew', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Cabin Crew');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Hotel Manager', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Hotel Manager');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Chef', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Chef');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Journalist', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Journalist');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Photographer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Photographer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Actor', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Actor');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Singer', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Singer');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Dentist', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Dentist');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Pharmacist', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Pharmacist');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Nurse', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Nurse');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Physiotherapist', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Physiotherapist');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Self Employed', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Self Employed');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Student', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Student');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Not Working', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Not Working');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Retired', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Retired');

INSERT INTO occupations (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM occupations WHERE name = 'Other');

--Genders
INSERT INTO genders
(admin_id, name, is_active, created_at, updated_at)

SELECT
    1,
    'Male',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE LOWER(name) = LOWER('Male')
);

INSERT INTO genders
(admin_id, name, is_active, created_at, updated_at)

SELECT
    1,
    'Female',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE LOWER(name) = LOWER('Female')
);

INSERT INTO genders
(admin_id, name, is_active, created_at, updated_at)

SELECT
    1,
    'Other',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE LOWER(name) = LOWER('Other')
);

-- ==========================================
-- CITIES
-- ==========================================

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Maharashtra'),
'Mumbai',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Mumbai'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Maharashtra'),
'Pune',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Pune'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Maharashtra'),
'Nagpur',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Nagpur'
-- Cities (major Indian cities)
INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mumbai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
SELECT 1 FROM cities WHERE name = 'Mumbai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Delhi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Delhi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'New Delhi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'New Delhi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bangalore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bangalore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hyderabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hyderabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ahmedabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ahmedabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Chennai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Chennai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kolkata',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kolkata'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Pune',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Pune'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jaipur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jaipur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Surat',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Surat'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Lucknow',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Lucknow'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kanpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kanpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nagpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nagpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Indore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Indore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Thane',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Thane'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhopal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhopal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Visakhapatnam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Visakhapatnam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Patna',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Patna'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Vadodara',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Vadodara'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ghaziabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ghaziabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ludhiana',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ludhiana'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Agra',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Agra'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nashik',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nashik'
);
INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Faridabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Faridabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Meerut',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Meerut'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rajkot',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rajkot'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kalyan',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kalyan'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Vasai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Vasai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Varanasi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Varanasi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Srinagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Srinagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ch Sambhajinagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ch Sambhajinagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dhanbad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dhanbad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Amritsar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Amritsar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Navi Mumbai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Navi Mumbai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Allahabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Allahabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Prayagraj',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Prayagraj'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ranchi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ranchi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Howrah',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Howrah'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Coimbatore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Coimbatore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jabalpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jabalpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gwalior',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gwalior'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Vijayawada',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Vijayawada'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jodhpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jodhpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Madurai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Madurai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Raipur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Raipur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kota',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kota'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Guwahati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Guwahati'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Chandigarh',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Chandigarh'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Solapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Solapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hubli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hubli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mysore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mysore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tiruchirappalli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tiruchirappalli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bareilly',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bareilly'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Aligarh',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Aligarh'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tiruppur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tiruppur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Moradabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Moradabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jalandhar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jalandhar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhubaneswar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhubaneswar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Salem',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Salem'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Warangal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Warangal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Guntur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Guntur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhiwandi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhiwandi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Saharanpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Saharanpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gorakhpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gorakhpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bikaner',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bikaner'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Amravati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Amravati'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Noida',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Noida'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jamshedpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jamshedpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhilai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhilai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Cuttack',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Cuttack'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Firozabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Firozabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kochi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kochi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nellore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nellore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhavnagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhavnagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dehradun',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dehradun'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Durgapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Durgapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Asansol',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Asansol'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rourkela',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rourkela'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nanded',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nanded'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kolhapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kolhapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ajmer',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ajmer'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Akola',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Akola'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gulbarga',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gulbarga'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jamnagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jamnagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ujjain',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ujjain'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Loni',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Loni'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Siliguri',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Siliguri'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jhansi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jhansi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ulhasnagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ulhasnagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jammu',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jammu'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sangli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sangli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mangalore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mangalore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Erode',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Erode'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Belgaum',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Belgaum'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ambattur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ambattur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tirunelveli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tirunelveli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Malegaon',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Malegaon'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gaya',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gaya'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jalgaon',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jalgaon'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Udaipur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Udaipur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Maheshtala',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Maheshtala'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Davanagere',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Davanagere'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kozhikode',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kozhikode'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kurnool',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kurnool'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rajpur Sonarpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rajpur Sonarpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rajahmundry',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rajahmundry'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bokaro',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bokaro'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'South Dumdum',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'South Dumdum'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bellary',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bellary'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Patiala',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Patiala'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gopalpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gopalpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Agartala',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Agartala'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhagalpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhagalpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Muzaffarnagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Muzaffarnagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhatpara',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhatpara'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Panihati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Panihati'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Latur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Latur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dhule',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dhule'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rohtak',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rohtak'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Korba',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Korba'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhilwara',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhilwara'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Berhampur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Berhampur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Muzaffarpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Muzaffarpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ahilyanagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ahilyanagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mathura',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mathura'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kollam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kollam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Avadi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Avadi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kadapa',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kadapa'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Anantapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Anantapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kamarhati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kamarhati'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bilaspur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bilaspur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Shahjahanpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Shahjahanpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Satara',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Satara'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bijapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bijapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rampur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rampur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Shivamogga',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Shivamogga'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Chandrapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Chandrapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Junagadh',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Junagadh'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Thrissur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Thrissur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Alwar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Alwar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bardhaman',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bardhaman'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kulti',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kulti'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kakinada',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kakinada'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nizamabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nizamabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Parbhani',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Parbhani'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tumkur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tumkur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Khammam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Khammam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ozhukarai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ozhukarai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bihar Sharif',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bihar Sharif'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Panipat',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Panipat'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Darbhanga',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Darbhanga'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bally',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bally'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Aizawl',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Aizawl'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dewas',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dewas'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ichalkaranji',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ichalkaranji'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Karnal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Karnal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bathinda',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bathinda'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jalna',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jalna'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Eluru',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Eluru'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Barasat',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Barasat'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kirari Suleman Nagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kirari Suleman Nagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Purnia',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Purnia'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Satna',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Satna'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mau',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mau'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sonipat',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sonipat'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Farrukhabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Farrukhabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rourkela',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rourkela'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Durg',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Durg'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Imphal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Imphal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ratlam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ratlam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Arrah',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Arrah'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Karimnagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Karimnagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Anand',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Anand'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Etawah',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Etawah'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ambarnath',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ambarnath'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'North Dumdum',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'North Dumdum'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bharatpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bharatpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Begusarai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Begusarai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'New Town Kolkata',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'New Town Kolkata'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gandhidham',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gandhidham'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Baranagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Baranagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tiruvottiyur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tiruvottiyur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Puducherry',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Puducherry'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sikar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sikar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Thoothukudi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Thoothukudi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Rewa',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Rewa'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mirzapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mirzapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Raichur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Raichur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Pali',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Pali'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ramagundam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ramagundam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Haridwar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Haridwar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Vijayanagaram',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Vijayanagaram'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Katihar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Katihar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nagercoil',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nagercoil'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sri Ganganagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sri Ganganagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Karawal Nagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Karawal Nagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mango',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mango'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Thanjavur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Thanjavur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bulandshahr',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bulandshahr'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Uluberia',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Uluberia'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Murwara',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Murwara'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sambhal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sambhal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Singrauli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Singrauli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nadiad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nadiad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Secunderabad',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Secunderabad'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Naihati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Naihati'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Yamunanagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Yamunanagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bidhan Nagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bidhan Nagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Pallavaram',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Pallavaram'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bidar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bidar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Munger',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Munger'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Panchkula',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Panchkula'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Burhanpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Burhanpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Raurkela',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Raurkela'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kharagpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kharagpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dindigul',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dindigul'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gandhinagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gandhinagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hospet',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hospet'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Nangloi Jat',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Nangloi Jat'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Malda',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Malda'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Ongole',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Ongole'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Deoghar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Deoghar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Chapra',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Chapra'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Haldia',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Haldia'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Khandwa',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Khandwa'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Morena',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Morena'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Amroha',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Amroha'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Anantnag',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Anantnag'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Etah',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Etah'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Chittoor',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Chittoor'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhind',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhind'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Raiganj',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Raiganj'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhusawal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhusawal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Orai',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Orai'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bahraich',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bahraich'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Phusro',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Phusro'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Vellore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Vellore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Mehsana',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Mehsana'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Raebareli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Raebareli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sirsa',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sirsa'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Danapur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Danapur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Serampore',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Serampore'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sultan Pur Majra',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sultan Pur Majra'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Guna',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Guna'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jaunpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jaunpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Panvel',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Panvel'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Shivpuri',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Shivpuri'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Surendranagar',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Surendranagar'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Unnao',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Unnao'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hugli',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hugli'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Alappuzha',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Alappuzha'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kottayam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kottayam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Machilipatnam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Machilipatnam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Shimla',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Shimla'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Adoni',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Adoni'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tenali',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tenali'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Proddatur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Proddatur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Saharsa',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Saharsa'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hindupur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hindupur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Sasaram',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Sasaram'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Hajipur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Hajipur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bhimavaram',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bhimavaram'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kumbakonam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kumbakonam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dehri',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dehri'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Madanapalle',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Madanapalle'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Siwan',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Siwan'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bettiah',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bettiah'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Guntakal',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Guntakal'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Srikakulam',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Srikakulam'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Motihari',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Motihari'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Dharmavaram',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Dharmavaram'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Gudivada',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Gudivada'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Narasaraopet',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Narasaraopet'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Bagaha',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Bagaha'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Miryalaguda',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Miryalaguda'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tadipatri',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tadipatri'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kishanganj',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kishanganj'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Karaikudi',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Karaikudi'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Suryapet',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Suryapet'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Jamalpur',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Jamalpur'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Kavali',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Kavali'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Tadepalligudem',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Tadepalligudem'
);

INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
    'Amaravati',
    2,
    1,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name = 'Amaravati'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Gujarat'),
'Ahmedabad',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Ahmedabad'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Gujarat'),
'Surat',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Surat'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Karnataka'),
'Bangalore',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Bangalore'
);

INSERT INTO cities
(admin_id, state_id, name, is_active, created_at, updated_at)

SELECT
NULL,
(SELECT id FROM states WHERE name = 'Karnataka'),
'Mysore',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Mysore'
);
INSERT INTO cities
(name, state_id, admin_id, is_active, created_at, updated_at)

SELECT
'Mysore',
(SELECT id FROM states WHERE name='Karnataka' LIMIT 1),
NULL,
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM cities WHERE name='Mysore'
);
-- Mother Tongues
INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Hindi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Hindi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Marathi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Marathi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Gujarati', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Gujarati');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Punjabi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Punjabi');

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
SELECT NULL, 'Malayalam', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Malayalam');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Urdu', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Urdu');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Odia', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Odia');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Assamese', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Assamese');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Konkani', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Konkani');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Sindhi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Sindhi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Kashmiri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Kashmiri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Dogri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Dogri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Manipuri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Manipuri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bodo', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bodo');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Maithili', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Maithili');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Sanskrit', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Sanskrit');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Santhali', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Santhali');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Nepali', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Nepali');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Rajasthani', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Rajasthani');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Haryanvi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Haryanvi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bhojpuri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bhojpuri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Magahi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Magahi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Chhattisgarhi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Chhattisgarhi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Garhwali', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Garhwali');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Kumaoni', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Kumaoni');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Tulu', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Tulu');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Kodava', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Kodava');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Mizo', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Mizo');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Khasi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Khasi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Garo', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Garo');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Lepcha', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Lepcha');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bhili', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bhili');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Gondi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Gondi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Ho', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Ho');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Kurukh', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Kurukh');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Mundari', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Mundari');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Angika', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Angika');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Awadhi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Awadhi');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bagheli', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bagheli');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Bundeli', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Bundeli');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Marwari', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Marwari');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Pahari', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Pahari');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Nagpuri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Nagpuri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Surjapuri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Surjapuri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Khortha', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Khortha');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Meitei', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Meitei');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Tripuri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Tripuri');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Nicobarese', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Nicobarese');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Toda', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Toda');

INSERT INTO mother_tongues (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM mother_tongues WHERE name = 'Other');

--castes

-- ==========================================
-- RELIGIONS
-- ==========================================

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Hindu', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Hindu'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Muslim', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Muslim'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Christian', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Christian'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Sikh', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Sikh'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Buddhist', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Buddhist'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Jain', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Jain'
);

INSERT INTO religions (admin_id, name, is_active, created_at, updated_at)
SELECT NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM religions WHERE name = 'Other'
);

-- ==========================================
-- CASTES
-- ==========================================

INSERT INTO castes
(admin_id, religion_id, name, is_active, created_at, updated_at)

SELECT NULL, 1, 'Brahmin', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Brahmin'
);

INSERT INTO castes
(admin_id, religion_id, name, is_active, created_at, updated_at)

SELECT NULL, 1, 'Maratha', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Maratha'
);
INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Brahmin', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Brahmin');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Maratha', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Maratha');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kunbi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kunbi');

SELECT NULL, 2, 'Sunni', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Sunni'
);
INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Yadav', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Yadav');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Rajput', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Rajput');

SELECT NULL, 2, 'Shia', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Shia'
);
INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Jat', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Jat');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Patel', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Patel');

SELECT NULL, 3, 'Roman Catholic', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Roman Catholic'
);
INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kurmi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kurmi');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Gupta', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Gupta');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Agarwal', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Agarwal');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kshatriya', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kshatriya');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Vaishya', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Vaishya');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kayastha', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kayastha');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Reddy', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Reddy');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kamma', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kamma');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Naidu', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Naidu');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Chettiar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Chettiar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Nair', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Nair');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Menon', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Menon');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Ezhava', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Ezhava');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Vokkaliga', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Vokkaliga');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Lingayat', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Lingayat');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Gowda', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Gowda');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Baniya', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Baniya');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Arora', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Arora');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Saini', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Saini');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Khatri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Khatri');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Mali', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Mali');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Teli', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Teli');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Lohana', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Lohana');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Bhandari', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Bhandari');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Sonar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Sonar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Sutar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Sutar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Lohar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Lohar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Kumbhar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Kumbhar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Nhavi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Nhavi');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Shimpi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Shimpi');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Gavli', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Gavli');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Dhangar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Dhangar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Mahar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Mahar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Matang', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Matang');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Chambhar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Chambhar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Bhoi', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Bhoi');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Beldar', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Beldar');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Koli', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Koli');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Agri', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Agri');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Leva Patil', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Leva Patil');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Jain Bania', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Jain Bania');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Oswal', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Oswal');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Porwal', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Porwal');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Maheshwari', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Maheshwari');

INSERT INTO castes (admin_id, religion_id, name, is_active, created_at, updated_at)
SELECT 1, 1, 'Other', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (SELECT 1 FROM castes WHERE name = 'Other');

-- ==========================================
-- SUB CASTES
-- ==========================================

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
(SELECT id FROM admins LIMIT 1),

(SELECT id FROM castes
 WHERE name = 'Brahmin'
 LIMIT 1),

'Deshastha Brahmin',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1
    FROM sub_castes
    WHERE name = 'Deshastha Brahmin'
);
-- ==========================================
-- HEIGHTS
-- ==========================================
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Deshastha Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Deshastha Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Chitpavan Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Chitpavan Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Karhade Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Karhade Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Saraswat Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Saraswat Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Iyer',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Iyer'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Iyengar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Iyengar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Namboodiri',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Namboodiri'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Gaur Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Gaur Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Kanyakubj Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kanyakubj Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Brahmin'),
    'Maithil Brahmin',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Maithil Brahmin'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    '96 Kuli Maratha',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = '96 Kuli Maratha'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Kunbi Maratha',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kunbi Maratha'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Leva Patil',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Leva Patil'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Deshmukh',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Deshmukh'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Bhosale',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Bhosale'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Pawar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Pawar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Shinde',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Shinde'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Jadhav',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Jadhav'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'More',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'More'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maratha'),
    'Patil',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Patil'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Patel'),
    'Kadva Patel',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kadva Patel'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Patel'),
    'Leuva Patel',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Leuva Patel'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Patel'),
    'Patidar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Patidar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Kurmi'),
    'Kurmi Patel',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kurmi Patel'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Yadav'),
    'Yaduvanshi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Yaduvanshi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Yadav'),
    'Ahir',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Ahir'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Yadav'),
    'Ghosi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Ghosi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Yadav'),
    'Nandvanshi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Nandvanshi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Sisodia',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Sisodia'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Rathore',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Rathore'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Chauhan',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Chauhan'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Solanki',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Solanki'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Parmar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Parmar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Rajput'),
    'Tomar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Tomar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Jat'),
    'Jat Sikh',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Jat Sikh'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Jat'),
    'Jat Hindu',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Jat Hindu'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Agarwal'),
    'Agarwal Bania',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Agarwal Bania'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Jain'),
    'Oswal Jain',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Oswal Jain'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Maheshwari'),
    'Maheshwari',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Maheshwari'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Jain'),
    'Porwal Jain',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Porwal Jain'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Agarwal'),
    'Khandelwal',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Khandelwal'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Reddy'),
    'Reddy Balija',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Reddy Balija'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Naidu'),
    'Kamma Naidu',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kamma Naidu'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Nair'),
    'Nair Menon',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Nair Menon'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Nair'),
    'Pillai',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Pillai'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Nair'),
    'Kurup',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kurup'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Lingayat'),
    'Lingayat Banajiga',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Lingayat Banajiga'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Lingayat'),
    'Lingayat Panchamasali',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Lingayat Panchamasali'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Vokkaliga'),
    'Vokkaliga Gowda',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Vokkaliga Gowda'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Vokkaliga'),
    'Hallikar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Hallikar'
);
INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Reddy'),
    'Velama',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Velama'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Reddy'),
    'Kapu',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kapu'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sonar'),
    'Sonar Daivadnya',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Sonar Daivadnya'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sonar'),
    'Tambat',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Tambat'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sonar'),
    'Kasar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kasar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sutar'),
    'Sutar Vishwakarma',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Sutar Vishwakarma'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Lohar'),
    'Lohar Panchal',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Lohar Panchal'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Kumbhar'),
    'Kumbhar Prajapati',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Kumbhar Prajapati'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Mali'),
    'Mali Phulmali',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Mali Phulmali'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Mali'),
    'Mali Jire',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Mali Jire'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Mali'),
    'Mali Saini',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Mali Saini'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Dhangar'),
    'Dhangar Hatkar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Dhangar Hatkar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Dhangar'),
    'Dhangar Khutekar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Dhangar Khutekar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Koli'),
    'Koli Mahadev',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Koli Mahadev'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Koli'),
    'Koli Malhar',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Koli Malhar'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Agri'),
    'Agri Koli',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Agri Koli'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Mahar'),
    'Mahar Neo Buddhist',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Mahar Neo Buddhist'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Matang'),
    'Matang Mang',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Matang Mang'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Chambhar'),
    'Chambhar Mochi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Chambhar Mochi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Bohra'),
    'Bohra Dawoodi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Bohra Dawoodi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Khoja'),
    'Khoja Ismaili',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Khoja Ismaili'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Syed'),
    'Syed Sunni',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Syed Sunni'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Pathan'),
    'Pathan Khan',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Pathan Khan'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sheikh'),
    'Sheikh Qureshi',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Sheikh Qureshi'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Ansari'),
    'Ansari Julaha',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Ansari Julaha'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sikh Jat'),
    'Ramgarhia Sikh',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Ramgarhia Sikh'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Sikh Jat'),
    'Ahluwalia Sikh',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Ahluwalia Sikh'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Arora'),
    'Arora Khatri',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Arora Khatri'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Meena'),
    'Meena Tribal',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Meena Tribal'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Gond'),
    'Gond Raj Gond',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Gond Raj Gond'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Bhil'),
    'Bhil Pawra',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Bhil Pawra'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Santhal'),
    'Santhal Murmu',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Santhal Murmu'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Munda'),
    'Munda Ho',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Munda Ho'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Oraon'),
    'Oraon Kurukh',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Oraon Kurukh'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Christian Roman Catholic'),
    'Roman Catholic',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Roman Catholic'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Christian Protestant'),
    'CSI Christian',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'CSI Christian'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Christian Protestant'),
    'Pentecostal Christian',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Pentecostal Christian'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Christian Protestant'),
    'Protestant Christian',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Protestant Christian'
);

INSERT INTO sub_castes
(admin_id, caste_id, name, is_active, created_at, updated_at)

SELECT
    1,
    (SELECT id FROM castes WHERE name = 'Other'),
    'Other',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
    WHERE NOT EXISTS (
SELECT 1 FROM sub_castes WHERE name = 'Other'
);

-- ==========================================
-- HEIGHTS
-- ==========================================

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 0in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 1in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 1in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 2in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 2in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 3in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 3in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 4in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 4in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 5in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 5in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 6in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 6in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 7in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 7in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 8in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 8in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 9in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 9in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 10in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 10in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 11in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 11in'
);
INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 0in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 1in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 1in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 2in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 2in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 3in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 3in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 4in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 4in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 5in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 5in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 6in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 6in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 7in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 7in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 8in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 8in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 9in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 9in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 10in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 10in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 11in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 11in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 0in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 1in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 1in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 2in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 2in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 3in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 3in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 4in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 4in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 5in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 5in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 6in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 6in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 7in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 7in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 8in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 8in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 9in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 9in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 10in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 10in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 11in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 11in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '7ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='7ft 0in'
);
-- ==========================================
-- WEIGHTS
-- ==========================================

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '35 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='35 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '36 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='36 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '37 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='37 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '38 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='38 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '39 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='39 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '40 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='40 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '41 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='41 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '42 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='42 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '43 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='43 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '44 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='44 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '45 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='45 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '46 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='46 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '47 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='47 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '48 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='48 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '49 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='49 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '50 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='50 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '51 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='51 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '52 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='52 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '53 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='53 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '54 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='54 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '55 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='55 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '56 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='56 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '57 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='57 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '58 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='58 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '59 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='59 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '60 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='60 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '61 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='61 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '62 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='62 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '63 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='63 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '64 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='64 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '65 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='65 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '66 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='66 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '67 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='67 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '68 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='68 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '69 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='69 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '70 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='70 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '71 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='71 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '72 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='72 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '73 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='73 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '74 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='74 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '75 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='75 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '76 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='76 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '77 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='77 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '78 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='78 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '79 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='79 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '80 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='80 Kg'
);

INSERT INTO genders (name, is_active, created_at, updated_at)
SELECT 'Male', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE name = 'Male'
);

INSERT INTO genders (name, is_active, created_at, updated_at)
SELECT 'Female', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE name = 'Female'
);

INSERT INTO genders (name, is_active, created_at, updated_at)
SELECT 'Other', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM genders WHERE name = 'Other'
);
-- ==========================================
-- COMPLEXIONS
-- ==========================================

INSERT INTO complexions
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Fair', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM complexions WHERE value='Fair'
);

INSERT INTO complexions
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Wheatish', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM complexions WHERE value='Wheatish'
);

INSERT INTO complexions
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Dark', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM complexions WHERE value='Dark'
);

INSERT INTO complexions
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Very Fair', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM complexions WHERE value='Very Fair'
);
-- ==========================================
-- BODY TYPES
-- ==========================================

INSERT INTO body_types
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Slim', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM body_types WHERE value='Slim'
);

INSERT INTO body_types
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Average', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM body_types WHERE value='Average'
);

INSERT INTO body_types
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Athletic', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM body_types WHERE value='Athletic'
);

INSERT INTO body_types
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, 'Heavy', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM body_types WHERE value='Heavy'
);

-- ==========================================
-- COUNTRIES
-- ==========================================

INSERT INTO countries
(admin_id, name, is_active, created_at, updated_at)

SELECT
NULL,
'India',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM countries
    WHERE name = 'India'
);

INSERT INTO countries
(admin_id, name, is_active, created_at, updated_at)

SELECT
NULL,
'USA',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM countries
    WHERE name = 'USA'
);

-- ==========================================
-- STATES
-- ==========================================

INSERT INTO states
(admin_id, country_id, name, is_active, created_at, updated_at)

SELECT
NULL,

(
    SELECT id
    FROM countries
    WHERE name = 'India'
    LIMIT 1
),

'Maharashtra',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM states
    WHERE name = 'Maharashtra'
);

INSERT INTO states
(admin_id, country_id, name, is_active, created_at, updated_at)

SELECT
NULL,

(
    SELECT id
    FROM countries
    WHERE name = 'India'
    LIMIT 1
),

'Gujarat',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM states
    WHERE name = 'Gujarat'
);

INSERT INTO states
(admin_id, country_id, name, is_active, created_at, updated_at)

SELECT
NULL,

(
    SELECT id
    FROM countries
    WHERE name = 'India'
    LIMIT 1
),

'Karnataka',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM states
    WHERE name = 'Karnataka'
);
--- ==========================================
 -- INCOMES
 -- ==========================================

 INSERT INTO incomes
 (admin_id, range, is_active, created_at, updated_at)

 SELECT NULL, '1 Lakh - 2 Lakhs', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
 WHERE NOT EXISTS (
     SELECT 1 FROM incomes WHERE range='1 Lakh - 2 Lakhs'
 );

 INSERT INTO incomes
 (admin_id, range, is_active, created_at, updated_at)

 SELECT NULL, '2 Lakhs - 5 Lakhs', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
 WHERE NOT EXISTS (
     SELECT 1 FROM incomes WHERE range='2 Lakhs - 5 Lakhs'
 );

 INSERT INTO incomes
 (admin_id, range, is_active, created_at, updated_at)

 SELECT NULL, '5 Lakhs - 10 Lakhs', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
 WHERE NOT EXISTS (
     SELECT 1 FROM incomes WHERE range='5 Lakhs - 10 Lakhs'
 );

 INSERT INTO incomes
 (admin_id, range, is_active, created_at, updated_at)

 SELECT NULL, '10 Lakhs+', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
 WHERE NOT EXISTS (
     SELECT 1 FROM incomes WHERE range='10 Lakhs+'
 );
-- ==========================================
-- DIETS
-- ==========================================

INSERT INTO diets
(admin_id, name, is_active, created_at, updated_at)

SELECT NULL, 'Vegetarian', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM diets WHERE name='Vegetarian'
);

INSERT INTO diets
(admin_id, name, is_active, created_at, updated_at)

SELECT NULL, 'Non Vegetarian', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM diets WHERE name='Non Vegetarian'
);

INSERT INTO diets
(admin_id, name, is_active, created_at, updated_at)

SELECT NULL, 'Eggetarian', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM diets WHERE name='Eggetarian'
);

INSERT INTO diets
(admin_id, name, is_active, created_at, updated_at)

SELECT NULL, 'Vegan', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM diets WHERE name='Vegan'
);
-- ==========================================
-- SMOKING
-- ==========================================

INSERT INTO smoking
(admin_id, value, is_active, created_at, updated_at)

SELECT
NULL,
'Non-Smoker',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM smoking WHERE value='Non-Smoker'
);

INSERT INTO smoking
(admin_id, value, is_active, created_at, updated_at)

SELECT
NULL,
'Occasional Smoker',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM smoking WHERE value='Occasional Smoker'
);

INSERT INTO smoking
(admin_id, value, is_active, created_at, updated_at)

SELECT
NULL,
'Regular Smoker',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM smoking WHERE value='Regular Smoker'
);

-- ==========================================
-- DRINKING
-- ==========================================

INSERT INTO drinking
(admin_id, name, value, is_active, created_at, updated_at)

SELECT
NULL,
'Drinking Habit',
'Non-Drinker',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM drinking WHERE value='Non-Drinker'
);

INSERT INTO drinking
(admin_id, name, value, is_active, created_at, updated_at)

SELECT
NULL,
'Drinking Habit',
'Occasionally',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM drinking WHERE value='Occasionally'
);

INSERT INTO drinking
(admin_id, name, value, is_active, created_at, updated_at)

SELECT
NULL,
'Drinking Habit',
'Regularly',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
    SELECT 1 FROM drinking WHERE value='Regularly'
);
-- ==========================================
-- PROFILE TYPES
-- ==========================================

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Self',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Self'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Son',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Son'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Daughter',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Daughter'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Brother',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Brother'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Sister',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Sister'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Friend',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Friend'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Relative',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Relative'
);

INSERT INTO profile_types
(admin_id,name,is_active,created_at,updated_at)

SELECT
NULL,
'Other',
TRUE,
CURRENT_TIMESTAMP,
CURRENT_TIMESTAMP

WHERE NOT EXISTS (
SELECT 1 FROM profile_types
WHERE name='Other'
);
-- =====================================================
-- PROFILE TYPES
-- =====================================================

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Self',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Self'
);

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Son',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Son'
);

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Daughter',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Daughter'
);

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Brother',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Brother'
);

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Sister',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Sister'
);

INSERT INTO profile_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Friend',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM profile_types WHERE name='Friend'
);

-- =====================================================
-- MANGLIK STATUS
-- =====================================================

INSERT INTO manglik_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Manglik',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM manglik_status WHERE name='Manglik'
);

INSERT INTO manglik_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Non Manglik',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM manglik_status WHERE name='Non Manglik'
);

INSERT INTO manglik_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Partial Manglik',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM manglik_status WHERE name='Partial Manglik'
);

-- =====================================================
-- FAMILY TYPE
-- =====================================================

INSERT INTO family_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Joint Family',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_types WHERE name='Joint Family'
);

INSERT INTO family_types (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Nuclear Family',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_types WHERE name='Nuclear Family'
);

-- =====================================================
-- FAMILY STATUS
-- =====================================================

INSERT INTO family_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Middle Class',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_status WHERE name='Middle Class'
);

INSERT INTO family_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Upper Middle Class',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_status WHERE name='Upper Middle Class'
);

INSERT INTO family_status (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Rich / Affluent',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_status WHERE name='Rich / Affluent'
);

-- =====================================================
-- FAMILY VALUES
-- =====================================================

INSERT INTO family_values (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Traditional',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_values WHERE name='Traditional'
);

INSERT INTO family_values (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Moderate',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_values WHERE name='Moderate'
);

INSERT INTO family_values (admin_id,name,is_active,created_at,updated_at)
SELECT NULL,'Liberal',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM family_values WHERE name='Liberal'
);
-- =====================================================
-- QUALIFICATIONS
-- =====================================================

INSERT INTO qualifications
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'BCA',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM qualifications WHERE name='BCA'
);

INSERT INTO qualifications
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'BTech',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM qualifications WHERE name='BTech'
);

INSERT INTO qualifications
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'MCA',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM qualifications WHERE name='MCA'
);

INSERT INTO qualifications
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'MBA',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM qualifications WHERE name='MBA'
);

-------------------------------------------------------

-- FIELD OF STUDY

INSERT INTO fields_of_study
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Computer Science',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM fields_of_study
WHERE name='Computer Science'
);

INSERT INTO fields_of_study
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Information Technology',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM fields_of_study
WHERE name='Information Technology'
);

INSERT INTO fields_of_study
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Mechanical',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM fields_of_study
WHERE name='Mechanical'
);

-------------------------------------------------------

-- EMPLOYED

INSERT INTO employed
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Student',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM employed
WHERE name='Student'
);

INSERT INTO employed
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Private Employee',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM employed
WHERE name='Private Employee'
);

INSERT INTO employed
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Government Employee',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM employed
WHERE name='Government Employee'
);

INSERT INTO employed
(admin_id,name,is_active,created_at,updated_at)

SELECT NULL,'Business',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM employed
WHERE name='Business'
);

-------------------------------------------------------

-- DISABILITY STATUS

INSERT INTO disability_statuses
(admin_id,value,is_active,created_at,updated_at)

SELECT NULL,'No Disability',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM disability_statuses
WHERE value='No Disability'
);

INSERT INTO disability_statuses
(admin_id,value,is_active,created_at,updated_at)

SELECT NULL,'Physically Challenged',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM disability_statuses
WHERE value='Physically Challenged'
);-------------------------------------------------------

-- BLOOD GROUPS

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'A+',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='A+'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'A-',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='A-'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'B+',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='B+'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'B-',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='B-'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'AB+',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='AB+'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'AB-',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='AB-'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'O+',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='O+'
);

INSERT INTO blood_groups
(admin_id,type,is_active,created_at,updated_at)

SELECT NULL,'O-',TRUE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP
WHERE NOT EXISTS (
SELECT 1 FROM blood_groups
WHERE type='O-'
);
INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '81 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='81 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '82 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='82 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '83 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='83 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '84 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='84 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '85 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='85 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '86 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='86 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '87 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='87 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '88 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='88 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '89 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='89 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '90 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='90 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '91 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='91 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '92 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='92 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '93 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='93 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '94 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='94 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '95 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='95 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '96 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='96 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '97 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='97 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '98 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='98 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '99 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='99 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '100 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='100 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '101 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='101 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '102 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='102 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '103 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='103 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '104 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='104 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '105 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='105 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '106 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='106 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '107 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='107 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '108 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='108 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '109 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='109 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '110 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='110 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '111 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='111 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '112 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='112 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '113 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='113 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '114 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='114 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '115 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='115 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '116 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='116 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '117 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='117 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '118 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='118 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '119 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='119 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '120 Kg', TRUE, CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='120 Kg'
);

-- =========================================
-- COMPLEXIONS MASTER DATA
-- =========================================

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Fair',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Fair')
);

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Very Fair',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Very Fair')
);

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Wheatish',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Wheatish')
);

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Wheatish Brown',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Wheatish Brown')
);

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Dark',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Dark')
);

INSERT INTO complexions
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Medium',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM complexions
    WHERE LOWER(value) = LOWER('Medium')
);

-- =========================================
-- BODY TYPES MASTER DATA
-- =========================================

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Slim',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Slim')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Average',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Average')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Athletic',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Athletic')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Fit',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Fit')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Muscular',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Muscular')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Heavy',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Heavy')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Lean',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Lean')
);

INSERT INTO body_types
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Medium',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1
    FROM body_types
    WHERE LOWER(value) = LOWER('Medium')
);

-- =========================================
-- ANNUAL INCOME MASTER DATA
-- =========================================

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    'Below 1 Lakh',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('Below 1 Lakh')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '1 - 3 Lakhs',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('1 - 3 Lakhs')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '3 - 5 Lakhs',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('3 - 5 Lakhs')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '5 - 10 Lakhs',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('5 - 10 Lakhs')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '10 - 20 Lakhs',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('10 - 20 Lakhs')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '20 - 50 Lakhs',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('20 - 50 Lakhs')
);

INSERT INTO annual_incomes
(admin_id, value, status, is_active, created_at, updated_at)

SELECT
    1,
    '50 Lakhs and Above',
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP,
    NULL

    WHERE NOT EXISTS (
    SELECT 1 FROM annual_incomes
    WHERE LOWER(value) = LOWER('50 Lakhs and Above')
);
