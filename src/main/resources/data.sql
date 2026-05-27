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

INSERT INTO castes
(admin_id, religion_id, name, is_active, created_at, updated_at)

SELECT NULL, 2, 'Sunni', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Sunni'
);

INSERT INTO castes
(admin_id, religion_id, name, is_active, created_at, updated_at)

SELECT NULL, 2, 'Shia', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Shia'
);

INSERT INTO castes
(admin_id, religion_id, name, is_active, created_at, updated_at)

SELECT NULL, 3, 'Roman Catholic', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM castes WHERE name = 'Roman Catholic'
);

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
-- ==========================================
-- HEIGHTS
-- ==========================================

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '4ft 5in', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='4ft 5in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 0in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '5ft 5in', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='5ft 5in'
);

INSERT INTO heights
(admin_id, height, is_active, created_at, updated_at)

SELECT NULL, '6ft 0in', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM heights WHERE height='6ft 0in'
);

-- ==========================================
-- WEIGHTS
-- ==========================================

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '40 Kg', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='40 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '50 Kg', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='50 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '60 Kg', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='60 Kg'
);

INSERT INTO weights
(admin_id, value, is_active, created_at, updated_at)

SELECT NULL, '70 Kg', TRUE, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (
SELECT 1 FROM weights WHERE value='70 Kg'
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