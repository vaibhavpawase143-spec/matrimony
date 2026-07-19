-- =====================================================
-- GATHBANDHAN MATRIMONY
-- OCCUPATION MASTER DATA
-- =====================================================

INSERT INTO occupations (
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
    o.occupation_name,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM (
    VALUES

    -- IT & Software
    ('Software Engineer'),
    ('Senior Software Engineer'),
    ('Java Developer'),
    ('Python Developer'),
    ('Full Stack Developer'),
    ('Frontend Developer'),
    ('Backend Developer'),
    ('Mobile App Developer'),
    ('Web Developer'),
    ('DevOps Engineer'),
    ('Cloud Engineer'),
    ('Data Engineer'),
    ('Data Scientist'),
    ('Data Analyst'),
    ('AI / ML Engineer'),
    ('Cyber Security Analyst'),
    ('Database Administrator'),
    ('System Administrator'),
    ('Network Engineer'),
    ('QA Engineer'),
    ('UI/UX Designer'),
    ('Technical Architect'),
    ('Project Manager'),
    ('Product Manager'),

    -- Engineering
    ('Civil Engineer'),
    ('Mechanical Engineer'),
    ('Electrical Engineer'),
    ('Electronics Engineer'),
    ('Chemical Engineer'),
    ('Industrial Engineer'),
    ('Production Engineer'),
    ('Automobile Engineer'),
    ('Mining Engineer'),
    ('Marine Engineer'),
    ('Architect'),

    -- Medical
    ('Doctor'),
    ('Surgeon'),
    ('Dentist'),
    ('Orthodontist'),
    ('Pharmacist'),
    ('Physiotherapist'),
    ('Nurse'),
    ('Lab Technician'),
    ('Radiologist'),
    ('Medical Officer'),
    ('Veterinarian'),

    -- Education
    ('Teacher'),
    ('Assistant Professor'),
    ('Professor'),
    ('Principal'),
    ('Lecturer'),
    ('Research Scholar'),
    ('Research Scientist'),

    -- Finance & Commerce
    ('Chartered Accountant'),
    ('Cost Accountant'),
    ('Company Secretary'),
    ('Financial Analyst'),
    ('Investment Banker'),
    ('Bank Manager'),
    ('Bank Employee'),
    ('Auditor'),
    ('Accountant'),
    ('Tax Consultant'),

    -- Legal
    ('Lawyer'),
    ('Advocate'),
    ('Judge'),
    ('Legal Advisor'),

    -- Government
    ('Government Employee'),
    ('Civil Servant'),
    ('IAS Officer'),
    ('IPS Officer'),
    ('IFS Officer'),
    ('IRS Officer'),
    ('Police Officer'),
    ('Forest Officer'),
    ('Postal Employee'),
    ('Railway Employee'),

    -- Defence
    ('Army Officer'),
    ('Army Personnel'),
    ('Navy Officer'),
    ('Air Force Officer'),
    ('Defence Personnel'),

    -- Business
    ('Business Owner'),
    ('Entrepreneur'),
    ('Managing Director'),
    ('CEO'),
    ('Director'),
    ('Partner'),
    ('Trader'),
    ('Wholesaler'),
    ('Retailer'),
    ('Shop Owner'),

    -- Sales & Marketing
    ('Sales Executive'),
    ('Sales Manager'),
    ('Marketing Executive'),
    ('Marketing Manager'),
    ('Business Development Manager'),
    ('Digital Marketing Specialist'),

    -- Media & Creative
    ('Journalist'),
    ('Editor'),
    ('Content Writer'),
    ('Graphic Designer'),
    ('Animator'),
    ('Photographer'),
    ('Videographer'),
    ('Fashion Designer'),
    ('Interior Designer'),

    -- Hospitality
    ('Hotel Manager'),
    ('Chef'),
    ('Restaurant Owner'),
    ('Travel Consultant'),
    ('Tour Operator'),

    -- Aviation & Shipping
    ('Pilot'),
    ('Cabin Crew'),
    ('Air Traffic Controller'),
    ('Merchant Navy Officer'),

    -- Agriculture
    ('Farmer'),
    ('Agricultural Officer'),
    ('Dairy Farmer'),
    ('Horticulturist'),

    -- Skilled Workers
    ('Electrician'),
    ('Plumber'),
    ('Carpenter'),
    ('Mechanic'),
    ('Technician'),

    -- Healthcare Support
    ('Health Worker'),
    ('Caregiver'),

    -- Others
    ('Consultant'),
    ('Freelancer'),
    ('Self Employed'),
    ('Private Employee'),
    ('Public Sector Employee'),
    ('Student'),
    ('Retired'),
    ('Homemaker'),
    ('Not Working'),
    ('Other')

) AS o(occupation_name)

CROSS JOIN (
    SELECT id
    FROM admins
    WHERE username = 'superadmin'
    LIMIT 1
) a

WHERE NOT EXISTS (
    SELECT 1
    FROM occupations occ
    WHERE occ.admin_id = a.id
      AND LOWER(occ.name) = LOWER(o.occupation_name)
);

-- -----------------------------------------------------
-- VERIFY (Development Only)
-- -----------------------------------------------------
-- SELECT COUNT(*) FROM occupations;
-- SELECT * FROM occupations ORDER BY name;