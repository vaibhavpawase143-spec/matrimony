-- =====================================================
-- GATHBANDHAN MATRIMONY
-- FIELD OF STUDY MASTER DATA
-- =====================================================

INSERT INTO fields_of_study (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at,
    deleted_at,
    deleted_by
)
SELECT
    a.id,
    f.field_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES

    -- Engineering & Technology
    ('Computer Science'),
    ('Information Technology'),
    ('Artificial Intelligence'),
    ('Data Science'),
    ('Cyber Security'),
    ('Software Engineering'),
    ('Mechanical Engineering'),
    ('Civil Engineering'),
    ('Electrical Engineering'),
    ('Electronics Engineering'),
    ('Electronics & Communication'),
    ('Chemical Engineering'),
    ('Automobile Engineering'),
    ('Production Engineering'),
    ('Industrial Engineering'),
    ('Aeronautical Engineering'),
    ('Aerospace Engineering'),
    ('Marine Engineering'),
    ('Mining Engineering'),
    ('Environmental Engineering'),

    -- Medical & Healthcare
    ('Medicine'),
    ('Dentistry'),
    ('Pharmacy'),
    ('Nursing'),
    ('Physiotherapy'),
    ('Occupational Therapy'),
    ('Ayurveda'),
    ('Homeopathy'),
    ('Unani'),
    ('Veterinary Science'),
    ('Radiology'),
    ('Medical Laboratory Technology'),

    -- Commerce & Management
    ('Commerce'),
    ('Accounting'),
    ('Finance'),
    ('Banking'),
    ('Business Administration'),
    ('Human Resource Management'),
    ('Marketing'),
    ('International Business'),
    ('Economics'),

    -- Science
    ('Physics'),
    ('Chemistry'),
    ('Mathematics'),
    ('Statistics'),
    ('Biotechnology'),
    ('Biochemistry'),
    ('Microbiology'),
    ('Botany'),
    ('Zoology'),
    ('Environmental Science'),
    ('Food Technology'),

    -- Computer Applications
    ('Computer Applications'),

    -- Arts & Humanities
    ('History'),
    ('Political Science'),
    ('Geography'),
    ('Sociology'),
    ('Psychology'),
    ('Philosophy'),
    ('Public Administration'),
    ('English'),
    ('Hindi'),
    ('Marathi'),
    ('Sanskrit'),
    ('Urdu'),

    -- Law
    ('Law'),

    -- Agriculture
    ('Agriculture'),
    ('Horticulture'),
    ('Forestry'),
    ('Dairy Technology'),

    -- Architecture & Design
    ('Architecture'),
    ('Interior Design'),
    ('Fashion Design'),
    ('Graphic Design'),
    ('Animation'),
    ('Fine Arts'),

    -- Media
    ('Journalism'),
    ('Mass Communication'),
    ('Film Studies'),
    ('Photography'),

    -- Hospitality & Tourism
    ('Hotel Management'),
    ('Travel & Tourism'),
    ('Culinary Arts'),

    -- Aviation
    ('Aviation'),
    ('Nautical Science'),

    -- Education
    ('Education'),

    -- Others
    ('Other')

) AS f(field_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM fields_of_study fs
    WHERE fs.admin_id = a.id
      AND LOWER(fs.name) = LOWER(f.field_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM fields_of_study;
-- SELECT * FROM fields_of_study ORDER BY name;